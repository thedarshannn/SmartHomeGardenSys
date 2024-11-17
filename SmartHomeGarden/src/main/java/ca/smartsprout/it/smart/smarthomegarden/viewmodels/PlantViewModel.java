/**
 * Smart Sprout
 * Members:
 * 1. Aditi Patel, n01525570, CENG322-RCB
 * 2. Birava Prajapati, n01579924, CENG322-RCA
 * 3. Darshankumar Prajapati, n01584247, CENG322-RCB
 * 4. Zeel Patel, n01526282, CENG322-RCB
 */
package ca.smartsprout.it.smart.smarthomegarden.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import ca.smartsprout.it.smart.smarthomegarden.BuildConfig;
import ca.smartsprout.it.smart.smarthomegarden.data.model.Plant;
import ca.smartsprout.it.smart.smarthomegarden.data.model.PlantResponse;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PlantViewModel extends ViewModel {
    private final MutableLiveData<List<Plant>> plantList = new MutableLiveData<>();
    private static final String API_URL = "https://perenual.com/api/species-list?key=sk-lhd4673423e3776287615";
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();

    public PlantViewModel() {
        loadPlants();
    }

    public LiveData<List<Plant>> getPlantList() {
        return plantList;
    }

    private void loadPlants() {
        // Build the request
        Request request = new Request.Builder()
                .url(API_URL)
                .build();

        // Make the API call
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    // Parse JSON response
                    String jsonResponse = response.body().string();
                    Type responseType = new TypeToken<PlantResponse>() {}.getType();
                    PlantResponse plantResponse = gson.fromJson(jsonResponse, responseType);


                    // Update the LiveData with the data
                    if (plantResponse != null) {
                        plantList.postValue(plantResponse.getData());
                    }
                } else {
                    plantList.postValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                plantList.postValue(null);  // Set to null on failure
            }
        });
    }
}
