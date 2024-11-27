package ca.smartsprout.it.smart.smarthomegarden.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ca.smartsprout.it.smart.smarthomegarden.data.model.PlantTask;
import ca.smartsprout.it.smart.smarthomegarden.data.repository.PlantTaskRepository;

public class PlantTaskViewModel extends ViewModel {
    private MutableLiveData<List<PlantTask>> tasks;
    private PlantTaskRepository repository;

    public PlantTaskViewModel() {
        repository = new PlantTaskRepository();
        tasks = new MutableLiveData<>(repository.getTasks());
    }

    public LiveData<List<PlantTask>> getTasks() {
        return tasks;
    }

    public void addTask(PlantTask task) {
        repository.addTask(task);
        tasks.setValue(repository.getTasks());
    }

    public void removeTask(PlantTask task) {
        repository.removeTask(task);
        tasks.setValue(repository.getTasks());
    }
}
