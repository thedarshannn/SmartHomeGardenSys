package ca.smartsprout.it.smart.smarthomegarden.viewmodels;

import android.util.Log;
import androidx.lifecycle.ViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import ca.smartsprout.it.smart.smarthomegarden.data.model.SensorData;

public class SensorViewModel extends ViewModel {

    private final DatabaseReference mDatabase;
    private final FirebaseUser currentUser;

    public SensorViewModel() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void updateSensorData(SensorData sensorData) {
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userSensorsRef = mDatabase.child("Users").child(userId).child("sensors");

            userSensorsRef.child("sunlight").setValue(sensorData.getSunlight());
            userSensorsRef.child("temperature").setValue(sensorData.getTemperature());
            userSensorsRef.child("moisture").setValue(sensorData.getMoisture());

            Log.d("SensorViewModel", "Updated values - Sunlight: " + sensorData.getSunlight() +
                    " Temperature: " + sensorData.getTemperature() + " Moisture: " + sensorData.getMoisture());
        } else {
            Log.e("SensorViewModel", "No authenticated user found.");
        }
    }
}
