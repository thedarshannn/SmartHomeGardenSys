package ca.smartsprout.it.smart.smarthomegarden.ui;

import android.os.Bundle;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;


import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.utils.NetworkUtils;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.NetworkViewModel;

public class TaskHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the root view (FrameLayout)
        ViewGroup rootView = findViewById(android.R.id.content); // Use activity's root view

// Initialize ViewModel
        NetworkViewModel networkViewModel = new ViewModelProvider(this).get(NetworkViewModel.class);

// Observe network status
        networkViewModel.getIsConnected().observe(this, isConnected -> {
            if (isConnected) {
                // Device is online
                NetworkUtils.removeOfflineOverlay(rootView); // Remove offline overlay
            } else {
                // Device is offline
                NetworkUtils.showOfflineOverlay(this, rootView, this);
            }
        });

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

    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}