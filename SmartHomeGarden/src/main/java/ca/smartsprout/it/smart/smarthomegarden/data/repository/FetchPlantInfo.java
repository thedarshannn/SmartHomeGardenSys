package ca.smartsprout.it.smart.smarthomegarden.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import ca.smartsprout.it.smart.smarthomegarden.data.model.ChatGPTRequest;
import ca.smartsprout.it.smart.smarthomegarden.data.model.ChatGPTResponse;
import ca.smartsprout.it.smart.smarthomegarden.data.model.Plant;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FetchPlantInfo {
    private static final String TAG = "FetchPlantInfo";
    private final ChatGPTApiService apiService;

    // Constructor to initialize Retrofit service
    public FetchPlantInfo() {
        apiService = RetrofitClient.getInstance().create(ChatGPTApiService.class);
    }

    /**
     * Fetch plant information by sending a prompt to the ChatGPT API.
     *
     * @param plantName The name of the plant to search for.
     * @param callback  Callback to handle success or error.
     */
    public void getPlantInfo(String plantName, PlantInfoCallback callback) {
        ChatGPTRequest request = new ChatGPTRequest(plantName);

        Call<ChatGPTResponse> call = apiService.generatePlantInfo(request);
        call.enqueue(new Callback<ChatGPTResponse>() {
            @Override
            public void onResponse(@NonNull Call<ChatGPTResponse> call, @NonNull Response<ChatGPTResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        // Extract and log the response
                        String jsonResponse = response.body().getChoices().get(0).getMessage().getContent();
                        // Parse the JSON into a Plant object
                        Plant plant = parsePlantJson(plantName, jsonResponse);
                        callback.onSuccess(plant);
                    } catch (JSONException e) {
                        Log.e(TAG, "JSON Parsing Error: " + e.getMessage(), e);
                        callback.onError("JSON Parsing Error: " + e.getMessage());
                    } catch (Exception e) {
                        Log.e(TAG, "Unexpected Error: " + e.getMessage(), e);
                        callback.onError("Unexpected Error: " + e.getMessage());
                    }
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "No error body";
                        Log.e(TAG, "API Response Error: " + errorBody);
                        callback.onError("API Error: " + errorBody);
                    } catch (IOException e) {
                        Log.e(TAG, "Error reading error body: " + e.getMessage(), e);
                        callback.onError("Error reading API error body.");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ChatGPTResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "API Call Failure: " + t.getMessage(), t);
                callback.onError("API call failed: " + t.getMessage());
            }
        });
    }

    private Plant parsePlantJson(String plantName, String jsonResponse) throws JSONException {
        // Remove any extra formatting
        jsonResponse = jsonResponse.trim();
        if (jsonResponse.startsWith("```")) {
            jsonResponse = jsonResponse.replaceFirst("```json", "").replaceFirst("```", "").trim();
        }

        JSONObject plantInfoJson = new JSONObject(jsonResponse);
        return new Plant(
                plantName,
                plantInfoJson.optString("description", "Description not available."),
                plantInfoJson.optString("wateringPeriod", "Watering period not available."),
                plantInfoJson.optString("suitability", "Suitability information not available."),
                plantInfoJson.optString("toxicity", "Toxicity information not available.")
        );
    }


    public interface PlantInfoCallback {
        void onSuccess(Plant plant);
        void onError(String errorMessage);
    }
}
