package ca.smartsprout.it.smart.smarthomegarden.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ca.smartsprout.it.smart.smarthomegarden.data.model.Relay;
import ca.smartsprout.it.smart.smarthomegarden.data.repository.FirebaseRepository;


public class RelayViewModel extends ViewModel {

    private final DatabaseReference relayRef;
    private final MutableLiveData<String> relayState = new MutableLiveData<>();

    public RelayViewModel() {
        // Correct path to relay in the sensors node
        relayRef = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child("06lfxQNpEzefzDO5v5pzfo7JUAx2")
                .child("plants")
                .child("PlantID1")
                .child("sensors")
                .child("relay");

        // Load initial state from Firebase
        loadRelayState();
    }

    private void loadRelayState() {
        relayRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String state = snapshot.getValue(String.class);
                    relayState.setValue(state != null ? state : "off"); // Default to "off"
                } else {
                    relayState.setValue("off"); // If no data, default to "off"
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle error if needed
            }
        });
    }

    public LiveData<String> getRelayState() {
        return relayState;
    }

    public void updateRelayState(String newState) {
        relayRef.setValue(newState);
    }
}
