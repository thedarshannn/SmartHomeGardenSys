/**
 * Smart Sprout
 * Members:
 * 1. Aditi Patel, n01525570, CENG322-RCB
 * 2. Birava Prajapati, n01579924, CENG322-RCA
 * 3. Darshankumar Prajapati, n01574247, CENG322-RCB
 * 4. Zeel Patel, n01526282, CENG322-RCB
 */

package ca.smartsprout.it.smart.smarthomegarden.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.List;
import ca.smartsprout.it.smart.smarthomegarden.data.model.PlantTask;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class PlantTaskViewModel extends ViewModel {
    private MutableLiveData<List<PlantTask>> tasks;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    public PlantTaskViewModel() {
        tasks = new MutableLiveData<>(new ArrayList<>());
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            databaseReference = firebaseDatabase.getReference("Users").child(userId).child("plant_tasks");
            loadTasks();
        } else {
            // Handle the case where there is no authenticated user
        }
    }

    public LiveData<List<PlantTask>> getTasks() {
        return tasks;
    }

    private void loadTasks() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<PlantTask> taskList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    PlantTask task = snapshot.getValue(PlantTask.class);
                    taskList.add(task);
                }
                tasks.setValue(taskList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }

    public void addTask(PlantTask task) {
        databaseReference.child(String.valueOf(task.getId())).setValue(task);
    }

    public void removeTask(PlantTask task) {
        databaseReference.child(String.valueOf(task.getId())).removeValue();
    }

    public void updateTask(PlantTask task) {
        databaseReference.child(String.valueOf(task.getId())).setValue(task);
    }
}
