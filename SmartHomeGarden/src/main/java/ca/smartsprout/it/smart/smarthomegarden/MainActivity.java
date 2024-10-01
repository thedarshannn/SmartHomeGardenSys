package ca.smartsprout.it.smart.smarthomegarden;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ca.smartsprout.it.smart.smarthomegarden.ui.fragments.DiagnoseFragment;
import ca.smartsprout.it.smart.smarthomegarden.ui.fragments.HomeFragment;
import ca.smartsprout.it.smart.smarthomegarden.ui.fragments.ProfileFragment;
import ca.smartsprout.it.smart.smarthomegarden.ui.fragments.SearchFragment;
import ca.smartsprout.it.smart.smarthomegarden.ui.fragments.SensorFragment;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.NavigationViewModel;

public class MainActivity extends AppCompatActivity {

    private NavigationViewModel navigationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        navigationViewModel = new ViewModelProvider(this).get(NavigationViewModel.class);

        // Set default fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, new HomeFragment())
                    .commit();
        }
        // Observe the selected item from ViewModel
        navigationViewModel.getSelectedItem().observe(this, itemId -> {
            Fragment selectedFragment = null;
            if (itemId == R.id.navigation_home) {
                selectedFragment = new HomeFragment();
            } else if (itemId == R.id.navigation_profile) {
                selectedFragment = new ProfileFragment();
            } else if (itemId == R.id.navigation_search) {
                selectedFragment = new SearchFragment();
            } else if (itemId == R.id.navigation_diagnosis) {
                selectedFragment = new DiagnoseFragment();
            } else if (itemId == R.id.navigation_sensor) {
                selectedFragment = new SensorFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, selectedFragment)
                        .commit();
            }
        });

        // Handle bottom navigation item clicks
        bottomNavigationView.setOnItemSelectedListener(item -> {
            navigationViewModel.setSelectedItem(item.getItemId());
            return true;
        });
    }

    // Alert dialog box which shows up when user click on back button.
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // Show exit confirmation dialog
            showExitConfirmationDialog();
            return true;  // Indicate that the event has been handled
        }
        return super.onKeyUp(keyCode, event);
    }

    /**
     * Displays a confirmation dialog asking the user if they want to exit the application.
     *
     * <p>The dialog shows a title, an icon, and a message prompting the user to confirm their choice.
     * The dialog has two buttons: "Yes" and "No". If the user selects "Yes", the application
     * will close using {@link #finish()}. If the user selects "No", the dialog will be dismissed.</p>
     *
     * <p>The dialog is non-cancelable, meaning the user must explicitly choose either "Yes" or "No".</p>
     */
    public void showExitConfirmationDialog() {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit");
        builder.setIcon(R.drawable.alert);
        builder.setMessage("Do you want to exit the application?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.create().show();
    }
}