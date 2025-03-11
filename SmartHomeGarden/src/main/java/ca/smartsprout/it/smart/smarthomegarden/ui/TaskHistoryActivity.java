package ca.smartsprout.it.smart.smarthomegarden.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

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
    private ImageView imageEmptyState;

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

        // Initialize Clear Button and Empty State Image
        buttonClear = findViewById(R.id.button_clear);
        imageEmptyState = findViewById(R.id.image_empty_state);

        // Observe task history data
        taskHistoryViewModel.getTaskHistory().observe(this, taskHistoryList -> {
            if (taskHistoryList != null && !taskHistoryList.isEmpty()) {
                // If there are tasks, show the RecyclerView and Clear button
                adapter.updateTaskHistory(taskHistoryList);
                recyclerView.setVisibility(View.VISIBLE);
                buttonClear.setVisibility(View.VISIBLE);
                imageEmptyState.setVisibility(View.GONE);
            } else {
                // If there are no tasks, show the empty state image and hide the RecyclerView and Clear button
                adapter.updateTaskHistory(new ArrayList<>());
                recyclerView.setVisibility(View.GONE);
                buttonClear.setVisibility(View.GONE);
                imageEmptyState.setVisibility(View.VISIBLE);
            }
        });

        // Set click listener for the Clear button
        buttonClear.setOnClickListener(v -> {
            taskHistoryViewModel.clearTaskHistory(); // Clear task history
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}