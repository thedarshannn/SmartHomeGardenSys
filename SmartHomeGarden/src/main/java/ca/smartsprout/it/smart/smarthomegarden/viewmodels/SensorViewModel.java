package ca.smartsprout.it.smart.smarthomegarden.viewmodels;

import android.util.Log;
import androidx.lifecycle.ViewModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import ca.smartsprout.it.smart.smarthomegarden.data.model.SensorData;

public class SensorViewModel extends ViewModel {

    private final DatabaseReference mDatabase;

    public SensorViewModel() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void updateSensorData(SensorData sensorData) {
        mDatabase.child("sensors").child("sunlight").setValue(sensorData.getSunlight());
        mDatabase.child("sensors").child("temperature").setValue(sensorData.getTemperature());
        mDatabase.child("sensors").child("moisture").setValue(sensorData.getMoisture());

        Log.d("SensorViewModel", "Updated values - Sunlight: " + sensorData.getSunlight() +
                " Temperature: " + sensorData.getTemperature() + " Moisture: " + sensorData.getMoisture());
    }
}
