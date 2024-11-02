package ca.smartsprout.it.smart.smarthomegarden.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.utils.Util;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.NotificationViewModel;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.SessionViewModel;

public class SettingsActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Handle back press with dispatcher
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish(); // Close the activity and go back
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Close the activity and go back
        return true;
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        private static final String TAG = "SettingsFragment";
        private ActivityResultLauncher<String> requestPermissionLauncher;
        private NotificationViewModel notificationViewModel;
        private SessionViewModel sessionViewModel;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            sessionViewModel = new ViewModelProvider(this).get(SessionViewModel.class);
            notificationViewModel = new ViewModelProvider(this).get(NotificationViewModel.class);

            // Initialize the permission launcher
            requestPermissionLauncher = registerForActivityResult(
                    new ActivityResultContracts.RequestPermission(),
                    isGranted -> {
                        if (isGranted) {
                            notificationViewModel.updateNotificationPermission(true);
                        } else {
                            notificationViewModel.updateNotificationPermission(false);
                            Util.showSnackbar(requireView(), "Notification permission denied. Please enable it in settings", true);
                        }
                    }
            );

            SwitchPreferenceCompat notificationToggle = findPreference("notifications");
            if (notificationToggle != null) {
                boolean isEnabled = getPreferenceManager().getSharedPreferences().getBoolean("notifications", false);
                notificationViewModel.updateNotificationPermission(isEnabled);

                notificationToggle.setOnPreferenceChangeListener((preference, newValue) -> {
                    boolean isEnabledToggle = (Boolean) newValue;
                    if (isEnabledToggle) {
                        Log.d(TAG, "Notification toggle enabled, checking permission");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            checkNotificationPermission();
                        }
                    } else {
                        Log.d(TAG, "Notification toggle disabled");
                        notificationViewModel.updateNotificationPermission(false);
                    }
                    return true;
                });
            }

            notificationViewModel.getNotificationPermissionState().observe(this, isGranted -> {
                if (notificationToggle != null) {
                    notificationToggle.setChecked(Boolean.TRUE.equals(isGranted));
                }
            });

            Preference logoutPreference = findPreference("logout");
            if (logoutPreference != null) {
                logoutPreference.setOnPreferenceClickListener(preference -> {
                    sessionViewModel.logOut();
                    return true;
                });
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
        private void checkNotificationPermission() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS)
                        != PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Requesting POST_NOTIFICATIONS permission");
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
                } else {
                    Log.d(TAG, "POST_NOTIFICATIONS permission already granted");
                    notificationViewModel.updateNotificationPermission(true);
                }
            }
        }
    }
}
