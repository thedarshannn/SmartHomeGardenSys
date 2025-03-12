package ca.smartsprout.it.smart.smarthomegarden.ui;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.utils.NetworkUtils;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.NetworkViewModel;

public class BaseActivity extends AppCompatActivity {

    private NetworkViewModel networkViewModel;
    private ViewGroup rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the root view
        rootView = findViewById(android.R.id.content);

        // Initialize ViewModel
        networkViewModel = new ViewModelProvider(this).get(NetworkViewModel.class);

//        // Observe connectivity status
//        networkViewModel.getIsConnected().observe(this, isConnected -> {
//            if (isConnected) {
//                // Remove the offline overlay if the internet is restored
//                NetworkUtils.removeOfflineOverlay(rootView);
//            } else {
//                // Show the offline overlay if the internet is not available
//                NetworkUtils.showOfflineOverlay(this, rootView);
//            }
//        });

        // Apply the screen orientation when the activity starts
        applyScreenOrientation();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Apply the screen orientation when the activity resumes
        applyScreenOrientation();
    }

    /**
     * Applies the screen orientation based on user preferences.
     */
    void applyScreenOrientation() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String orientation = sharedPreferences.getString("screen_orientation", "auto");

        switch (orientation) {
            case "portrait":
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;
            case "landscape":
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
            default:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                break;
        }
    }
}