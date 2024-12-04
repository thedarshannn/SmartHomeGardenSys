/**
 * Smart Sprout
 * Members:
 * 1. Aditi Patel, n01525570, CENG322-RCB
 * 2. Birava Prajapati, n01579924, CENG322-RCA
 * 3. Darshankumar Prajapati, n01574247, CENG322-RCB
 * 4. Zeel Patel, n01526282, CENG322-RCB
 */

package ca.smartsprout.it.smart.smarthomegarden.data.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import ca.smartsprout.it.smart.smarthomegarden.data.model.Plant;

public class PlantRepository {

    private static final String TAG = "PlantRepository";
    private final FirebaseFirestore firebaseFirestore;
    private final FirebaseAuth firebaseAuth;
    private final MutableLiveData<List<Plant>> plantsLiveData = new MutableLiveData<>();

    public PlantRepository() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }


    /**
     * Get the CollectionReference for the user's plants
     * @return CollectionReference for the user's plants
     */
    private CollectionReference getUserPlantsCollection() {
        String userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        return firebaseFirestore.collection("users").document(userId).collection("plants");
    }


    /**
     * Fetch the user's plants from Firebase
     * @return LiveData object containing a list of plants
     */
    public LiveData<List<Plant>> fetchPlants() {
        getUserPlantsCollection().addSnapshotListener((querySnapshot, e) -> {
            if (e != null) {
                Log.e(TAG, "Error fetching plants: " + e.getMessage(), e);
                return;
            }

            if (querySnapshot != null) {
                List<Plant> plants = new ArrayList<>();
                for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                    Plant plant = document.toObject(Plant.class);
                    if (plant != null) {
                        plant.setId(document.getId());
                        plants.add(plant);
                    }
                }
                plantsLiveData.postValue(plants);
            }
        });

        return plantsLiveData;
    }



    /**
     * Add a plant to the user's collection
     * @param actualName The actual name of the plant
     * @param customName The custom name of the plant
     * @param dateAdded The date the plant was added
     * @param notes Additional notes about the plant
     * @param additionalDetails Additional details about the plant
     * @param listener Callback listener for success/failure
     */
    public void addPlant(
            String actualName,
            String customName,
            Date dateAdded,
            String notes,
            Map<String, Object> additionalDetails,
            OnPlantAddedListener listener
    ) {
        Map<String, Object> plantData = new HashMap<>();
        plantData.put("actualName", actualName);
        plantData.put("customName", customName);
        plantData.put("dateAdded", dateAdded);
        plantData.put("notes", notes);

        if (additionalDetails != null) {
            plantData.putAll(additionalDetails);
        }

        getUserPlantsCollection().add(plantData)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Plant added successfully: " + documentReference.getId());
                    listener.onSuccess();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error adding plant: " + e.getMessage(), e);
                    listener.onFailure(e);
                });
    }



    /**
     * Delete a plant from the user's collection
     * @param plantId The ID of the plant to delete
     * @param listener Callback listener for success/failure
     */
    public void deletePlant(String plantId, OnPlantDeletedListener listener) {
        getUserPlantsCollection().document(plantId).delete()
                .addOnSuccessListener(aVoid -> listener.onSuccess())
                .addOnFailureListener(listener::onFailure);
    }

    /**
     * Listener interface for adding a plant
     */
    public interface OnPlantAddedListener {
        void onSuccess();

        void onFailure(Exception e);
    }

    /**
     * Listener interface for deleting a plant
     */
    public interface OnPlantDeletedListener {
        void onSuccess();

        void onFailure(Exception e);
    }
}
