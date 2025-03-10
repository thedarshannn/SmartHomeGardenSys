package ca.smartsprout.it.smart.smarthomegarden.viewmodels;

import androidx.lifecycle.ViewModel;


import ca.smartsprout.it.smart.smarthomegarden.data.model.PlantTaskHistory;
import ca.smartsprout.it.smart.smarthomegarden.data.repository.TaskHistoryRepository;

public class TaskHistoryViewModel extends ViewModel {
    private TaskHistoryRepository repository;

    public TaskHistoryViewModel() {
        repository = new TaskHistoryRepository();
    }

    public void addTaskHistory(PlantTaskHistory taskHistory) {
        repository.addTaskHistory(taskHistory);
    }
}

