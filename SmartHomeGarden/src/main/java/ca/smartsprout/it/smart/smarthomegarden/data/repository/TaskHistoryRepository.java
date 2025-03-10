package ca.smartsprout.it.smart.smarthomegarden.data.repository;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ca.smartsprout.it.smart.smarthomegarden.data.model.PlantTaskHistory;

public class TaskHistoryRepository {
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    public TaskHistoryRepository() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            databaseReference = firebaseDatabase.getReference("Users").child(userId).child("task_history");
        }
    }

    public void addTaskHistory(PlantTaskHistory taskHistory) {
        if (databaseReference != null) {
            databaseReference.child(taskHistory.getId()).setValue(taskHistory);
        }
    }
}