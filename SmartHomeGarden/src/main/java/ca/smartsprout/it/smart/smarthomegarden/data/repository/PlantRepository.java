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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
    private final FirebaseFirestore firestore;
    private final FirebaseDatabase realtimeDatabase;
    private final FirebaseAuth firebaseAuth;
    private final MutableLiveData<List<Plant>> plantsLiveData = new MutableLiveData<>();

    public PlantRepository() {
        firestore = FirebaseFirestore.getInstance();
        realtimeDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    /**
     * Get the Firestore CollectionReference for the user's plants
     * @return CollectionReference for Firestore plants
     */
    private CollectionReference getUserPlantsCollection() {
        String userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        return firestore.collection("users").document(userId).collection("plants");
    }

    /**
     * Get the Realtime Database Reference for the user's plants
     * @return DatabaseReference for Realtime Database plants
     */
    private DatabaseReference getUserRealtimePlantsRef() {
        String userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        return realtimeDatabase.getReference("Users").child(userId).child("plants");
    }

    /**
     * Fetch user's plants from Firestore and sync them to LiveData
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
     * Add a plant to Firestore and Realtime Database
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
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            listener.onFailure(new Exception("User not logged in"));
            return;
        }
        String userId = currentUser.getUid();

        Map<String, Object> plantData = new HashMap<>();
        plantData.put("actualName", actualName);
        plantData.put("customName", customName);
        plantData.put("dateAdded", dateAdded);
        plantData.put("notes", notes);

        if (additionalDetails != null) {
            plantData.putAll(additionalDetails);
        }

        // Store in Firestore
        getUserPlantsCollection().add(plantData)
                .addOnSuccessListener(documentReference -> {
                    String plantId = documentReference.getId();
                    plantData.put("id", plantId);

                    DatabaseReference plantRef = getUserRealtimePlantsRef().child(plantId);

                    Map<String, Object> sensorData = new HashMap<>();
                    sensorData.put("UV", 0);
                    sensorData.put("lux", 0);
                    sensorData.put("moisture", 0);

                    // **Store in Realtime Database**
                    plantRef.setValue(plantData)
                            .addOnSuccessListener(aVoid -> {
                                // **Initialize sensor node after plant data**
                                plantRef.child("sensors").setValue(sensorData)
                                        .addOnSuccessListener(aVoid2 -> listener.onSuccess())
                                        .addOnFailureListener(listener::onFailure);
                            })
                            .addOnFailureListener(listener::onFailure);
                })
                .addOnFailureListener(listener::onFailure);
    }


    /**
     * Delete a plant from Firestore and Realtime Database
     * @param plantId The ID of the plant to delete
     * @param listener Callback listener for success/failure
     */
    public void deletePlant(String plantId, OnPlantDeletedListener listener) {
        getUserPlantsCollection().document(plantId).delete()
                .addOnSuccessListener(aVoid -> {
                    getUserRealtimePlantsRef().child(plantId).removeValue()
                            .addOnSuccessListener(aVoid1 -> listener.onSuccess())
                            .addOnFailureListener(listener::onFailure);
                })
                .addOnFailureListener(listener::onFailure);
    }

    /**
     * Fetch plant count from Firestore
     * @param userId User ID
     * @param callback Callback interface for result
     */
    public void fetchPlantCount(String userId, PlantCountCallback callback) {
        getUserPlantsCollection().get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    int count = queryDocumentSnapshots.size();
                    callback.onSuccess(count);
                })
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
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

    /**
     * Interface for fetching plant count
     */
    public interface PlantCountCallback {
        void onSuccess(int count);
        void onError(String errorMessage);
    }
}
