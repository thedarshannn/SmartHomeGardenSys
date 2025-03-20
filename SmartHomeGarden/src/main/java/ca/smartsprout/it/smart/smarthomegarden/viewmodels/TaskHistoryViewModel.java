package ca.smartsprout.it.smart.smarthomegarden.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ca.smartsprout.it.smart.smarthomegarden.data.model.PlantTaskHistory;
import ca.smartsprout.it.smart.smarthomegarden.data.repository.TaskHistoryRepository;

public class TaskHistoryViewModel extends ViewModel {
    private TaskHistoryRepository repository;
    private MutableLiveData<List<PlantTaskHistory>> taskHistoryLiveData;

    public TaskHistoryViewModel() {
        repository = new TaskHistoryRepository();
        taskHistoryLiveData = new MutableLiveData<>();
        loadTaskHistory();
    }

    public LiveData<List<PlantTaskHistory>> getTaskHistory() {
        return taskHistoryLiveData;
    }

    private void loadTaskHistory() {
        repository.getTaskHistory().observeForever(taskHistoryList -> {
            taskHistoryLiveData.setValue(taskHistoryList);
        });
    }

    public void addTaskHistory(PlantTaskHistory taskHistory) {
        repository.addTaskHistory(taskHistory);
    }

    // Method to clear task history
    public void clearTaskHistory() {
        repository.clearTaskHistory();
    }
}