/**
 * Smart Sprout
 * Members:
 * 1. Aditi Patel, n01525570, CENG322-RCB
 * 2. Birava Prajapati, n01579924, CENG322-RCA
 * 3. Darshankumar Prajapati, n01584247, CENG322-RCB
 * 4. Zeel Patel, n01526282, CENG322-RCB
 */
package ca.smartsprout.it.smart.smarthomegarden.viewmodels;

// PlantViewModel.java
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.List;

import ca.smartsprout.it.smart.smarthomegarden.data.model.Plant;

public class PlantViewModel extends ViewModel {
    private final MutableLiveData<List<Plant>> plantList = new MutableLiveData<>();

    public PlantViewModel() {
        loadPlants(); // Load initial data
    }

    public LiveData<List<Plant>> getPlantList() {
        return plantList;
    }

    private void loadPlants() {
        // Dummy data for testing
        List<Plant> plants = new ArrayList<>();
        plants.add(new Plant("1", "Golden Pothos", "A hardy plant with heart-shaped leaves."));
        plants.add(new Plant("2", "Spider Plant", "A resilient plant with long, arching leaves."));
        plants.add(new Plant("3", "Peace Lily", "Known for its white blooms and air-purifying abilities."));

        // Set the dummy data to LiveData
        plantList.setValue(plants);
    }
}
