/**
 * Smart Sprout
 * Members:
 * 1. Aditi Patel, n01525570, CENG322-RCB
 * 2. Birava Prajapati, n01579924, CENG322-RCA
 * 3. Darshankumar Prajapati, n01574247, CENG322-RCB
 * 4. Zeel Patel, n01526282, CENG322-RCB
 */

package ca.smartsprout.it.smart.smarthomegarden.ui;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;

import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.NetworkViewModel;

public class OfflineActivity extends AppCompatActivity {

    private NetworkViewModel networkViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);

        MaterialButton retryButton = findViewById(R.id.buttonRetry);
        MaterialButton settingsButton = findViewById(R.id.buttonSettings);

        networkViewModel = new ViewModelProvider(this).get(NetworkViewModel.class);

        // Initialize NetworkViewModel
        networkViewModel = new ViewModelProvider(this).get(NetworkViewModel.class);

        // Observe connectivity status
        networkViewModel.getIsConnected().observe(this, isConnected -> {
            if (isConnected) {
                // Close OfflineActivity when internet is restored
                finish();
            }
        });

        // Retry button: Trigger a reconnection check
        retryButton.setOnClickListener(v -> networkViewModel.getIsConnected().observe(this, isConnected -> {
            if (isConnected) {
                finish(); // Close the activity if the network is restored
            }
        }));

        // Open Settings button: Redirect to device's network settings
        settingsButton.setOnClickListener(v -> startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS)));


    }
}