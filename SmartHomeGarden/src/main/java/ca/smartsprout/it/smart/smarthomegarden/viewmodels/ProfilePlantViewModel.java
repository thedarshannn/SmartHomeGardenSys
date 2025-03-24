package ca.smartsprout.it.smart.smarthomegarden.viewmodels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import ca.smartsprout.it.smart.smarthomegarden.data.model.SensorData;
import ca.smartsprout.it.smart.smarthomegarden.data.repository.SensorRepository;

public class ProfilePlantViewModel extends ViewModel {
    private final MutableLiveData<Map<String, SensorData>> sensorMap = new MutableLiveData<>(new HashMap<>());
    private final SensorRepository sensorRepository = new SensorRepository();

    public LiveData<Map<String, SensorData>> getSensorDataMap() {
        return sensorMap;
    }

    public void loadSensorDataForPlant(String plantId) {
        sensorRepository.getSensorData(plantId, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SensorData data = snapshot.getValue(SensorData.class);
                if (data != null) {
                    Map<String, SensorData> currentMap = sensorMap.getValue();
                    if (currentMap != null) {
                        currentMap.put(plantId, data);
                        sensorMap.postValue(currentMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ViewModel", "Failed to load sensor data: " + error.getMessage());
            }
        });
    }
}
