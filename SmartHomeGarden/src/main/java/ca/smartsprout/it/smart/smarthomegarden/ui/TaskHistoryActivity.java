package ca.smartsprout.it.smart.smarthomegarden.ui;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.ui.adapter.TaskHistoryAdapter;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.TaskHistoryViewModel;

public class TaskHistoryActivity extends AppCompatActivity {
    private TaskHistoryViewModel taskHistoryViewModel;
    private TaskHistoryAdapter adapter;
    private RecyclerView recyclerView;
    private Button buttonClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_history);

        // Initialize ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Handle the back button press
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_view_task_history);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize ViewModel
        taskHistoryViewModel = new ViewModelProvider(this).get(TaskHistoryViewModel.class);

        // Initialize Adapter
        adapter = new TaskHistoryAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // Observe task history data
        taskHistoryViewModel.getTaskHistory().observe(this, taskHistoryList -> {
            if (taskHistoryList != null) {
                adapter.updateTaskHistory(taskHistoryList);
            } else {
                adapter.updateTaskHistory(new ArrayList<>()); // Pass an empty list if null
            }
        });

        // Initialize Clear Button
        buttonClear = findViewById(R.id.button_clear);
        buttonClear.setOnClickListener(v -> {
            taskHistoryViewModel.clearTaskHistory(); // Call ViewModel method to clear history
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}