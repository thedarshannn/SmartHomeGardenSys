/**
 * Smart Sprout
 * Members:
 * 1. Aditi Patel, n01525570, CENG322-RCB
 * 2. Birava Prajapati, n01579924, CENG322-RCA
 * 3. Darshankumar Prajapati, n01584247, CENG322-RCB
 * 4. Zeel Patel, n01526282, CENG322-RCB
 */

package ca.smartsprout.it.smart.smarthomegarden.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import ca.smartsprout.it.smart.smarthomegarden.MainActivity;
import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.utils.Util;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.NotificationViewModel;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.SessionViewModel;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.ThemeViewModel;

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
        private ThemeViewModel themeViewModel;
        private SwitchPreferenceCompat notificationToggle;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            sessionViewModel = new ViewModelProvider(this).get(SessionViewModel.class);
            notificationViewModel = new ViewModelProvider(this).get(NotificationViewModel.class);
            themeViewModel = new ViewModelProvider(this).get(ThemeViewModel.class);

            Preference editProfilePref = findPreference("edit_profile");
            if (editProfilePref != null) {
                editProfilePref.setOnPreferenceClickListener(preference -> {
                    Intent intent = new Intent(getActivity(), AccountSettingsActivity.class);
                    startActivity(intent);
                    return true;
                });
            }

            // Initialize the permission launcher
            requestPermissionLauncher = registerForActivityResult(
                    new ActivityResultContracts.RequestPermission(),
                    isGranted -> {
                        if (isGranted) {
                            notificationViewModel.updateNotificationPermission(true);
                        } else {
                            notificationViewModel.updateNotificationPermission(false);
                            Util.showSnackbar(requireView(), getString(R.string.notification_permission_snackbar), true);
                        }
                    }
            );
            ListPreference themePreference = findPreference("app_theme");
            if (themePreference != null) {
                themePreference.setOnPreferenceChangeListener((preference, newValue) -> {
                    String themeValue = (String) newValue;
                    themeViewModel.setThemeMode(themeValue);
                    requireActivity().recreate();
                    return true;
                });
                themeViewModel.getThemeMode().observe(this, themeValue -> {
                    if (themePreference != null) {
                        themePreference.setValue(themeValue);
                    }
                });
            }
          notificationToggle = findPreference("notifications");
            if (notificationToggle != null) {
                notificationToggle.setOnPreferenceChangeListener((preference, newValue) -> {
                    boolean isEnabledToggle = (Boolean) newValue;

                    if (isEnabledToggle) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS)
                                        != PackageManager.PERMISSION_GRANTED) {
                            checkNotificationPermission();
                                    return false;
                        }
                    } else {
                        Intent intent = new Intent(android.provider.Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                                .putExtra(android.provider.Settings.EXTRA_APP_PACKAGE, requireContext().getPackageName());
                        startActivity(intent);

                        // Reflect disabled state in ViewModel
                        notificationViewModel.updateNotificationPermission(false);
                        return false;
                    }
                    return true;
                });
            }

            notificationViewModel.getNotificationPermissionState().observe(this, isGranted -> {
                if (notificationToggle != null) {
                    boolean currentPermissionState = notificationViewModel.isNotificationPermissionGranted(requireContext());
                    notificationToggle.setChecked(currentPermissionState);

                    // Update ViewModel only if there's a mismatch
                    if (isGranted != currentPermissionState) {
                        notificationViewModel.updateNotificationPermission(currentPermissionState);
                    }
                }
            });


            Preference logoutPreference = findPreference("logout");
            if (logoutPreference != null) {
                logoutPreference.setOnPreferenceClickListener(preference -> {
                    sessionViewModel.logOut();
                    return true;
                });
            }

            Preference Feedback = findPreference("feedback");
            if (Feedback != null) {
                Feedback.setOnPreferenceClickListener(preference -> {
                    startActivity(new Intent(requireActivity(), FeedbackActivity.class));
                    return true;
                });

            }
        }
        @Override
        public void onResume() {
            super.onResume();

            if (notificationToggle != null) {
                // Check the current permission state
                boolean currentPermissionState = notificationViewModel.isNotificationPermissionGranted(requireContext());

                // Update the toggle state
                notificationToggle.setChecked(currentPermissionState);

                // Ensure ViewModel state is synced
                notificationViewModel.updateNotificationPermission(currentPermissionState);

                 }
        }
            private void checkNotificationPermission () {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS)
                            != PackageManager.PERMISSION_GRANTED) {
                          requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
                    } else {
                           notificationViewModel.updateNotificationPermission(true);
                    }
                }
            }

        }
    }

