/**
 * Smart Sprout
 * Members:
 * 1. Aditi Patel, n01525570, CENG322-RCB
 * 2. Birava Prajapati, n01579924, CENG322-RCA
 * 3. Darshankumar Prajapati, n01574247, CENG322-RCB
 * 4. Zeel Patel, n01526282, CENG322-RCB
 */

package ca.smartsprout.it.smart.smarthomegarden.viewmodels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ca.smartsprout.it.smart.smarthomegarden.data.model.SensorData;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SensorViewModel extends ViewModel {
    private final MutableLiveData<SensorData> sensorData = new MutableLiveData<>();

    public LiveData<SensorData> getSensorData() {
        return sensorData;
    }

    /**
     * Set the plant ID to fetch sensor data from the Realtime Database
     * @param plantId The ID of the plant
     */
    public void setPlantId(String plantId) {
        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        DatabaseReference sensorRef = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(userId)
                .child("plants")
                .child(plantId)
                .child("sensors");

        sensorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SensorData data = snapshot.getValue(SensorData.class);
                sensorData.postValue(data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("SensorViewModel", "Sensor fetch failed: " + error.getMessage());
            }
        });
    }

    /**
     * Update the sensor command in the Realtime Database
     * @param plantId The ID of the plant
     */
    public void updateSensorCommand(String plantId) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            DatabaseReference commandRef = FirebaseDatabase.getInstance()
                    .getReference("Users")
                    .child(uid)
                    .child("command");

            Map<String, Object> command = new HashMap<>();
            command.put("plant", plantId);
            command.put("requestReading", true);

            commandRef.setValue(command);
        }
    }

}
