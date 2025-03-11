package ca.smartsprout.it.smart.smarthomegarden.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ca.smartsprout.it.smart.smarthomegarden.data.model.PlantTaskHistory;

public class TaskHistoryRepository {
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private MutableLiveData<List<PlantTaskHistory>> taskHistoryLiveData;

    public TaskHistoryRepository() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        taskHistoryLiveData = new MutableLiveData<>();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            databaseReference = firebaseDatabase.getReference("Users").child(userId).child("task_history");
            loadTaskHistory();
        }
    }

    public LiveData<List<PlantTaskHistory>> getTaskHistory() {
        return taskHistoryLiveData;
    }

    private void loadTaskHistory() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<PlantTaskHistory> taskHistoryList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    PlantTaskHistory taskHistory = snapshot.getValue(PlantTaskHistory.class);
                    if (taskHistory != null) {
                        taskHistoryList.add(taskHistory);
                    }
                }
                taskHistoryLiveData.setValue(taskHistoryList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }

    public void addTaskHistory(PlantTaskHistory taskHistory) {
        if (databaseReference != null) {
            databaseReference.child(taskHistory.getId()).setValue(taskHistory);
        }
    }
}