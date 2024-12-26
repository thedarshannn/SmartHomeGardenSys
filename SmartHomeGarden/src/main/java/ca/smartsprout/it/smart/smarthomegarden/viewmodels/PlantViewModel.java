/**
 * Smart Sprout
 * Members:
 * 1. Aditi Patel, n01525570, CENG322-RCB
 * 2. Birava Prajapati, n01579924, CENG322-RCA
 * 3. Darshankumar Prajapati, n01584247, CENG322-RCB
 * 4. Zeel Patel, n01526282, CENG322-RCB
 */
package ca.smartsprout.it.smart.smarthomegarden.viewmodels;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ca.smartsprout.it.smart.smarthomegarden.data.model.Plant;
import ca.smartsprout.it.smart.smarthomegarden.data.model.SearchResponse;
import ca.smartsprout.it.smart.smarthomegarden.data.repository.PlantRepository;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PlantViewModel extends ViewModel {
    private static final String TAG = "PlantViewModel";
    private static final String SEARCH_URL = "https://plant.id/api/v3/kb/plants/name_search";
    private static final String DETAIL_URL = "https://plant.id/api/v3/kb/plants/:";
    private static final String API_KEY = "wlVT5GVqwoL558AtbNTqjpvku7IEVmHMSvFAlaqD4K48EXy0Ve";

    private final MutableLiveData<PlantDetail> plantDetail = new MutableLiveData<>();
    private final PlantRepository plantRepository;
    private MutableLiveData<List<Plant>> plantList = new MutableLiveData<>(new ArrayList<>());
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();
    private final ExecutorService executorService = Executors.newFixedThreadPool(2);

    public LiveData<PlantDetail> getPlantDetail() {
        return plantDetail;
    }

    public LiveData<List<Plant>> getAllPlants() {
        return plantList;
    }

    public PlantViewModel() {
        plantRepository = new PlantRepository();
        plantList = new MutableLiveData<>();
        fetchPlantsFromFirestore(); // Fetch data from Firestore during initialization
    }


    /**
     * Search for a plant by name and fetch its details
     * @param query The plant name to search for
     */
    public void searchAndFetchPlantDetail(String query) {
        executorService.execute(() -> {
            try {
                String searchUrl = SEARCH_URL + "?q=" + query + "&limit=1";
                Request searchRequest = new Request.Builder()
                        .url(searchUrl)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Api-Key", API_KEY)
                        .build();

                Response searchResponse = client.newCall(searchRequest).execute();
                if (searchResponse.isSuccessful() && searchResponse.body() != null) {
                    String searchJson = searchResponse.body().string();
                    Log.d(TAG, "Raw Search JSON: " + searchJson);

                    // Deserialize JSON into SearchResponse
                    Type responseType = new TypeToken<SearchResponse>() {}.getType();
                    SearchResponse response = gson.fromJson(searchJson, responseType);

                    if (response != null && response.getEntities() != null && !response.getEntities().isEmpty()) {
                        String accessToken = response.getEntities().get(0).getAccessToken();
                        Log.d(TAG, "Search Result Access Token: " + accessToken);
                        fetchPlantDetail(accessToken); // Pass accessToken to fetch detail
                    } else {
                        Log.e(TAG, "Access Token is null or no entities found");
                        plantDetail.postValue(null);
                    }
                } else {
                    Log.e(TAG, "Search API failed. Code: " + searchResponse.code() + ", Message: " + searchResponse.message());
                    plantDetail.postValue(null);
                }

                searchResponse.close();
            } catch (IOException e) {
                Log.e(TAG, "Error during search query: " + e.getMessage(), e);
                plantDetail.postValue(null);
            }
        });
    }


    /**
     * Fetch plant details using the access token
     * @param accessToken The access token to fetch plant details
     */
    private void fetchPlantDetail(String accessToken) {
        executorService.execute(() -> {
            try {
                // Construct the detail API URL
                String detailUrl = DETAIL_URL + accessToken + "?details=common_names,url,description,image,watering,propagation_methods&language=en";

                // Build the GET request
                Request detailRequest = new Request.Builder()
                        .url(detailUrl)
                        .addHeader("Content-Type", "application/json") // Optional for GET, but harmless
                        .addHeader("Api-Key", API_KEY)
                        .build();

                // Execute the request
                Response detailResponse = client.newCall(detailRequest).execute();

                // Process the response
                if (detailResponse.isSuccessful() && detailResponse.body() != null) {
                    String detailJson = detailResponse.body().string();
                    Log.d(TAG, "Detail Response JSON: " + detailJson); // Log for debugging

                    // Parse the response into the PlantDetail model
                    PlantDetail detail = gson.fromJson(detailJson, PlantDetail.class);
                    if (detail != null) {
                        plantDetail.postValue(detail);
                    } else {
                        Log.e(TAG, "Parsed PlantDetail is null");
                        plantDetail.postValue(null);
                    }
                } else {
                    Log.e(TAG, "Detail API failed. Code: " + detailResponse.code() + ", Message: " + detailResponse.message());
                    plantDetail.postValue(null);
                }

                detailResponse.close();
            } catch (IOException e) {
                Log.e(TAG, "Error fetching plant detail: " + e.getMessage(), e);
                plantDetail.postValue(null);
            }
        });
    }


    /**
     * Add a plant to the list
     * @param plant The plant to add to the list
     */
    public void addPlant(Plant plant) {
        List<Plant> currentPlants = new ArrayList<>(Objects.requireNonNull(plantList.getValue()));
        currentPlants.add(plant);
        plantList.setValue(currentPlants); // Set a new list instance
    }

    /**
     * Fetch plants from Firestore and update the LiveData
     */
    private void fetchPlantsFromFirestore() {
        plantRepository.fetchPlants().observeForever(plantList::postValue);
    }

}