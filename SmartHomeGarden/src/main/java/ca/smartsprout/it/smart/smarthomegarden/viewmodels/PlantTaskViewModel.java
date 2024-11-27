package ca.smartsprout.it.smart.smarthomegarden.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.List;
import ca.smartsprout.it.smart.smarthomegarden.data.model.PlantTask;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class PlantTaskViewModel extends ViewModel {
    private MutableLiveData<List<PlantTask>> tasks;
    private DatabaseReference databaseReference;

    public PlantTaskViewModel() {
        tasks = new MutableLiveData<>(new ArrayList<>());
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("plant_tasks");
        loadTasks();
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
}
