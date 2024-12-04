/**
 * Smart Sprout
 * Members:
 * 1. Aditi Patel, n01525570, CENG322-RCB
 * 2. Birava Prajapati, n01579924, CENG322-RCA
 * 3. Darshankumar Prajapati, n01574247, CENG322-RCB
 * 4. Zeel Patel, n01526282, CENG322-RCB
 */

package ca.smartsprout.it.smart.smarthomegarden.data.repository;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import ca.smartsprout.it.smart.smarthomegarden.data.model.PlantTask;

public class PlantTaskRepository {
    private List<PlantTask> tasks = new ArrayList<>();
    private DatabaseReference databaseReference;

    public PlantTaskRepository() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("plant_tasks");
    }

    public List<PlantTask> getTasks() {
        return tasks;
    }

    public void addTask(PlantTask task) {
        tasks.add(task);
        databaseReference.child(String.valueOf(task.getId())).setValue(task);
    }

    public void removeTask(PlantTask task) {
        tasks.remove(task);
        databaseReference.child(String.valueOf(task.getId())).removeValue();
    }
}
