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
import java.util.Objects;


import ca.smartsprout.it.smart.smarthomegarden.data.model.Plant;
import ca.smartsprout.it.smart.smarthomegarden.data.repository.FetchPlantInfo;
import ca.smartsprout.it.smart.smarthomegarden.data.repository.PlantRepository;

public class PlantViewModel extends ViewModel {
    private static final String TAG = "PlantViewModel";

    private final MutableLiveData<List<Plant>> plantList = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Plant> plantDetail = new MutableLiveData<>();

    private final FetchPlantInfo fetchPlantInfo;

    public PlantViewModel() {
        fetchPlantInfo = new FetchPlantInfo();
    }

    /**
     * Get the list of plants.
     *
     * @return LiveData containing the list of plants.
     */
    public LiveData<List<Plant>> getAllPlants() {
        return plantList;
    }

    /**
     * Get the current plant details.
     *
     * @return LiveData containing the plant details.
     */
    public LiveData<Plant> getPlantDetail() {
        return plantDetail;
    }

    /**
     * Fetch plant details using ChatGPT API.
     *
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
     * Add a plant to the list.
     *
     * @param plant The plant to add to the list.
     */
    public void addPlant(Plant plant) {
        List<Plant> currentPlants = new ArrayList<>(Objects.requireNonNull(plantList.getValue()));
        currentPlants.add(plant);
        plantList.setValue(currentPlants); // Update the LiveData with a new list instance
    }

    /**
     * Fetch plants from Firestore and update the LiveData
     */
    private void fetchPlantsFromFirestore() {
        PlantRepository plantRepository = null;
        plantRepository.fetchPlants().observeForever(plantList::postValue);
    }
}

