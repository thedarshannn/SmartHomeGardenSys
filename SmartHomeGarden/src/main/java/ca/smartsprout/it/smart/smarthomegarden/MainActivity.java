/**
 * Smart Sprout
 * Members:
 * 1. Aditi Patel, n01525570, CENG322-RCB
 * 2. Birava Prajapati, n01579924, CENG322-RCA
 * 3. Darshankumar Prajapati, n01584247, CENG322-RCB
 * 4. Zeel Patel, n01526282, CENG322-RCB
 */

package ca.smartsprout.it.smart.smarthomegarden;


import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;

import ca.smartsprout.it.smart.smarthomegarden.viewmodels.MenuHandler;
import ca.smartsprout.it.smart.smarthomegarden.ui.BaseActivity;
import ca.smartsprout.it.smart.smarthomegarden.ui.fragments.DiagnoseFragment;
import ca.smartsprout.it.smart.smarthomegarden.ui.fragments.HomeFragment;
import ca.smartsprout.it.smart.smarthomegarden.ui.fragments.ProfileFragment;
import ca.smartsprout.it.smart.smarthomegarden.ui.fragments.SearchFragment;
import ca.smartsprout.it.smart.smarthomegarden.ui.fragments.SensorFragment;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.NavigationViewModel;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.ThemeViewModel;

public class MainActivity extends BaseActivity {

    private NavigationViewModel navigationViewModel;
    private ThemeViewModel themeViewModel;
    private MenuHandler menuHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        navigationViewModel = new ViewModelProvider(this).get(NavigationViewModel.class);
        themeViewModel = new ViewModelProvider(this).get(ThemeViewModel.class);

        menuHandler = new MenuHandler(this);

        // Set the theme based on the user's preference
        themeViewModel.getThemeMode().observe(this, themeMode -> {
            switch (themeMode) {
                case "light":
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    break;
                case "dark":
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    break;
                case "system":
                default:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                    break;
            }
        });

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return menuHandler.handleMenuItem(item) || super.onOptionsItemSelected(item);
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
        builder.setTitle(R.string.exit);
        builder.setIcon(R.drawable.alert);
        builder.setMessage(R.string.do_you_want_to_exit_the_application);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.create().show();
    }
}