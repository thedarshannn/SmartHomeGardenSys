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

import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Map;

import ca.smartsprout.it.smart.smarthomegarden.data.model.Plant;
import ca.smartsprout.it.smart.smarthomegarden.data.repository.FetchPlantInfo;
import ca.smartsprout.it.smart.smarthomegarden.data.repository.PlantRepository;

public class PlantViewModel extends ViewModel {
    private static final String TAG = "PlantViewModel";

    private final MutableLiveData<List<Plant>> plantList = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Plant> plantDetail = new MutableLiveData<>();
    private final PlantRepository plantRepository;
    private final FetchPlantInfo fetchPlantInfo;

    public PlantViewModel() {
        plantRepository = new PlantRepository();
        fetchPlantInfo = new FetchPlantInfo();
        fetchPlantsFromFirestore();
    }

    /**
     * Get the list of plants.
     * @return LiveData containing the list of plants.
     */
    public LiveData<List<Plant>> getAllPlants() {
        return plantList;
    }

    /**
     * Get the current plant details.
     * @return LiveData containing the plant details.
     */
    public LiveData<Plant> getPlantDetail() {
        return plantDetail;
    }

    /**
     * Fetch plant details using external API.
     * @param plantName The name of the plant to search for.
     */
    public void fetchPlantDetails(String plantName) {
        fetchPlantInfo.getPlantInfo(plantName, new FetchPlantInfo.PlantInfoCallback() {
            @Override
            public void onSuccess(Plant plant) {
                plantDetail.postValue(plant);
            }

            @Override
            public void onError(String errorMessage) {
                Log.e(TAG, "Error fetching plant details: " + errorMessage);
                plantDetail.postValue(null);
            }
        });
    }

    /**
     * Add a plant to Firestore & Realtime Database.
     * @param actualName The actual name of the plant.
     * @param customName The custom name assigned by the user.
     * @param dateAdded The date the plant was added.
     * @param notes Additional notes about the plant.
     * @param additionalDetails Extra details about the plant.
     */
    public void addPlant(String actualName, String customName, Date dateAdded, String notes, Map<String, Object> additionalDetails) {
        plantRepository.addPlant(actualName, customName, dateAdded, notes, additionalDetails, new PlantRepository.OnPlantAddedListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Plant added successfully!");
                fetchPlantsFromFirestore(); // Refresh plant list
            }

            @Override
            public void onFailure(Exception e) {
                Log.e(TAG, "Error adding plant", e);
            }
        });
    }

    /**
     * Delete a plant from Firestore & Realtime Database.
     * @param plantId The unique ID of the plant to delete.
     */
    public void deletePlant(String plantId) {
        plantRepository.deletePlant(plantId, new PlantRepository.OnPlantDeletedListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Plant deleted successfully!");
                fetchPlantsFromFirestore(); // Refresh plant list
            }

            @Override
            public void onFailure(Exception e) {
                Log.e(TAG, "Error deleting plant", e);
            }
        });
    }

    /**
     * Fetch plants from Firestore and update the LiveData.
     */
    private void fetchPlantsFromFirestore() {
        plantRepository.fetchPlants().observeForever(plantList::postValue);
    }

    /**
     * Fetch the total count of plants.
     * @param userId The unique user ID.
     */
    public void fetchPlantCount(String userId) {
        plantRepository.fetchPlantCount(userId, new PlantRepository.PlantCountCallback() {
            @Override
            public void onSuccess(int count) {
                Log.d(TAG, "Total Plants: " + count);
            }

            @Override
            public void onError(String errorMessage) {
                Log.e(TAG, "Error fetching plant count: " + errorMessage);
            }
        });
    }
}
