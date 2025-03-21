package ca.smartsprout.it.smart.smarthomegarden.viewmodels;
//Zeel Patel
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ca.smartsprout.it.smart.smarthomegarden.data.model.Relay;
import ca.smartsprout.it.smart.smarthomegarden.data.repository.FirebaseRepository;


public class RelayViewModel extends ViewModel {

    private DatabaseReference relayRef;
    private final MutableLiveData<String> relayState = new MutableLiveData<>();

    public RelayViewModel() {
        // Get current user's UID dynamically
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String uid = currentUser.getUid();
            relayRef = FirebaseDatabase.getInstance()
                    .getReference("Users")
                    .child(uid) // Dynamic UID
                    .child("plants")
                    .child("PlantID1")
                    .child("sensors")
                    .child("relay");

            // Load initial state from Firebase
            loadRelayState();
        } else {
            relayState.setValue("off"); // Default if no user logged in
        }
    }

    private void loadRelayState() {
        if (relayRef != null) {
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
    }

    public LiveData<String> getRelayState() {
        return relayState;
    }

    public void updateRelayState(String newState) {
        if (relayRef != null) {
            relayRef.setValue(newState);
        }
    }
}
