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
        // Set the dummy data to LiveData
    }
}
