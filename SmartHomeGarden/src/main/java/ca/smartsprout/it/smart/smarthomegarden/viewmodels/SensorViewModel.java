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

public class SensorViewModel extends ViewModel {

    private final DatabaseReference mDatabase;
    private final FirebaseUser currentUser;
    private final MutableLiveData<SensorData> sensorDataLiveData;

    public SensorViewModel() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        sensorDataLiveData = new MutableLiveData<>();
        fetchSensorData();
    }

    public LiveData<SensorData> getSensorData() {
        return sensorDataLiveData;
    }

    private void fetchSensorData() {
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userSensorsRef = mDatabase.child("Users").child(userId).child("sensors");

            userSensorsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    SensorData sensorData = snapshot.getValue(SensorData.class);
                    sensorDataLiveData.setValue(sensorData);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("SensorViewModel", "Failed to read sensor data.", error.toException());
                }
            });
        } else {
            Log.e("SensorViewModel", "No authenticated user found.");
        }
    }
}
