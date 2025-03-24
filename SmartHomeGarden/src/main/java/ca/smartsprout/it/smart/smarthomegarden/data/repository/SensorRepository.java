package ca.smartsprout.it.smart.smarthomegarden.data.repository;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SensorRepository {
    private final DatabaseReference rootRef;

    public SensorRepository() {
        rootRef = FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("plants");
    }

    public void getSensorData(String plantId, ValueEventListener listener) {
        rootRef.child(plantId).child("sensors").addValueEventListener(listener);
    }
}
