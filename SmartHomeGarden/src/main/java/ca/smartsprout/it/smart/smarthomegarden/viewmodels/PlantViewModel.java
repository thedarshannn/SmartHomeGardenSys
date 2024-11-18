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
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ca.smartsprout.it.smart.smarthomegarden.data.model.Plant;
import ca.smartsprout.it.smart.smarthomegarden.data.model.PlantResponse;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PlantViewModel extends ViewModel {
    private static final String TAG = "PlantViewModel";
    private static final String BASE_URL = "https://perenual.com/api/species-list?key=sk-lhd4673423e3776287615";
    private final MutableLiveData<List<Plant>> plantList = new MutableLiveData<>();
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();
    private final ExecutorService executorService = Executors.newFixedThreadPool(2);

    public PlantViewModel() {
        loadPlants();
    }

    public LiveData<List<Plant>> getPlantList() {
        return plantList;
    }

    public void loadPlants() {
        executorService.execute(() -> {
            try {
                Request request = new Request.Builder().url(BASE_URL).build();
                Response response = client.newCall(request).execute();

                if (response.isSuccessful() && response.body() != null) {
                    String jsonResponse = response.body().string();
                    Type responseType = new TypeToken<PlantResponse>() {}.getType();
                    PlantResponse plantResponse = gson.fromJson(jsonResponse, responseType);

                    if (plantResponse != null && plantResponse.getData() != null) {
                        plantList.postValue(plantResponse.getData());
                    } else {
                        Log.e(TAG, "Plant list response is empty or null");
                        plantList.postValue(null);
                    }
                } else {
                    Log.e(TAG, "Failed to load plants: " + response.code());
                    plantList.postValue(null);
                }

                response.close();
            } catch (IOException e) {
                Log.e(TAG, "Error loading plants: " + e.getMessage(), e);
                plantList.postValue(null);
            }
        });
    }

    public void searchPlants(String query) {
        String searchUrl = BASE_URL + "&q=" + query;

        executorService.execute(() -> {
            try {
                Request request = new Request.Builder().url(searchUrl).build();
                Response response = client.newCall(request).execute();

                if (response.isSuccessful() && response.body() != null) {
                    String jsonResponse = response.body().string();
                    Type responseType = new TypeToken<PlantResponse>() {}.getType();
                    PlantResponse plantResponse = gson.fromJson(jsonResponse, responseType);

                    if (plantResponse != null && plantResponse.getData() != null) {
                        plantList.postValue(plantResponse.getData());
                    } else {
                        Log.e(TAG, "Search response is empty or null");
                        plantList.postValue(null);
                    }
                } else {
                    Log.e(TAG, "Search failed with code: " + response.code());
                    plantList.postValue(null);
                }

                response.close();
            } catch (IOException e) {
                Log.e(TAG, "Search request error: " + e.getMessage(), e);
                plantList.postValue(null);
            }
        });
    }
}
