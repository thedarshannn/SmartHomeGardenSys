package ca.smartsprout.it.smart.smarthomegarden.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DiagnoseViewModel extends ViewModel {

    String plantId = "yjtjOghcp2gJ3UzT2GIY";
    public LiveData<Integer> getWaterLevel(String userId) {
        MutableLiveData<Integer> waterLevelLiveData = new MutableLiveData<>();
        FirebaseDatabase.getInstance().getReference("Users")
                .child(userId).child("plants").child(plantId)
                .child("sensors").child("water_level")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Integer level = snapshot.getValue(Integer.class);
                        waterLevelLiveData.setValue(level != null ? level : 0);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
        return waterLevelLiveData;
    }

    public LiveData<String> getPumpState(String userId) {
        MutableLiveData<String> pumpStateLiveData = new MutableLiveData<>();
        FirebaseDatabase.getInstance().getReference("Users")
                .child(userId).child("plants").child(plantId)
                .child("sensors").child("relay")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        pumpStateLiveData.setValue(snapshot.getValue(String.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
        return pumpStateLiveData;
    }

    public void updatePumpState(String userId, String state) {
        FirebaseDatabase.getInstance().getReference("Users")
                .child(userId).child("plants").child(plantId)
                .child("sensors").child("relay")
                .setValue(state);
    }
}
