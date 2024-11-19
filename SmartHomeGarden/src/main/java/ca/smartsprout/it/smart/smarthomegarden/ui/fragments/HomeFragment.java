/**
 * Smart Sprout
 * Members:
 * 1. Aditi Patel, n01525570, CENG322-RCB
 * 2. Birava Prajapati, n01579924, CENG322-RCA
 * 3. Darshankumar Prajapati, n01574247, CENG322-RCB
 * 4. Zeel Patel, n01526282, CENG322-RCB
 */
package ca.smartsprout.it.smart.smarthomegarden.ui.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextClock;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.data.model.WeatherResponse;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.WeatherViewModel;

public class HomeFragment extends Fragment {

    private WeatherViewModel weatherViewModel;
    private TextView tvHighTemp, tvLowTemp;
    private static final String PERMISSION_PREFS = "permission_preferences";
    private static final String PERMISSION_GRANTED_KEY = "permission_granted";

    private final ActivityResultLauncher<String[]> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                Boolean fineLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false);
                Boolean coarseLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false);

                if ((fineLocationGranted != null && fineLocationGranted) ||
                        (coarseLocationGranted != null && coarseLocationGranted)) {
                    savePermissionState(true);
                    startLocationUpdates();
                    Snackbar.make(requireView(), R.string.location_permission_granted, Snackbar.LENGTH_SHORT).show();
                } else {
                    handlePermissionDenied();
                }
            });

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weatherViewModel = new ViewModelProvider(this).get(WeatherViewModel.class);

        // If permission was previously granted, start updates
        if (isPermissionPreviouslyGranted()) {
            startLocationUpdates();
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        TextClock clockTC = view.findViewById(R.id.textClock);
        clockTC.setFormat12Hour(getString(R.string.date_format));

        TextView greetingTextView = view.findViewById(R.id.greetingTextView);
        String username = getString(R.string.sir);
        greetingTextView.setText(getString(R.string.hello) + username);

        tvHighTemp = view.findViewById(R.id.tv_high_temp);
        tvLowTemp = view.findViewById(R.id.tv_low_temp);
        CardView weatherCardView = view.findViewById(R.id.cardView);

        // Set click listener on CardView for initial permission request
        weatherCardView.setOnClickListener(v -> {
            if (!isPermissionPreviouslyGranted()) {
                requestPermissionLauncher.launch(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                });
            } else if (!isLocationEnabled()) {
                showLocationServicesDialog();
            }
        });

        Button buttonAdd = view.findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, new SearchFragment())
                    .addToBackStack(null)
                    .commit();

            BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation);
            bottomNavigationView.setSelectedItemId(R.id.navigation_search);
        });

        weatherViewModel.getWeatherData().observe(getViewLifecycleOwner(), weatherResponse -> {
            if (weatherResponse != null && weatherResponse.main != null) {
                updateTemperatureDisplay(weatherResponse);
            }
        });

        weatherViewModel.getIsCelsius().observe(getViewLifecycleOwner(), isCelsius -> {
            if (weatherViewModel.getWeatherData().getValue() != null) {
                updateTemperatureDisplay(weatherViewModel.getWeatherData().getValue());
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isPermissionPreviouslyGranted() && isLocationEnabled()) {
            startLocationUpdates();
        }
    }

    private boolean isPermissionPreviouslyGranted() {
        SharedPreferences prefs = requireContext().getSharedPreferences(PERMISSION_PREFS, Context.MODE_PRIVATE);
        return prefs.getBoolean(PERMISSION_GRANTED_KEY, false);
    }

    private void savePermissionState(boolean granted) {
        SharedPreferences prefs = requireContext().getSharedPreferences(PERMISSION_PREFS, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(PERMISSION_GRANTED_KEY, granted).apply();
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void startLocationUpdates() {
        if (!isPermissionPreviouslyGranted()) {
            return;
        }

        LocationManager locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            try {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 300000, 1000, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        weatherViewModel.fetchWeatherData(location.getLatitude(), location.getLongitude());
                    }

                    @Override
                    public void onProviderEnabled(String provider) {}

                    @Override
                    public void onProviderDisabled(String provider) {
                        if (isAdded()) {
                            Log.e(getString(R.string.weatherfragment), getString(R.string.location_provider_disabled));
                            showLocationServicesDialog();
                        }
                    }
                });

                // Get last known location for immediate update
                Location lastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (lastLocation != null) {
                    weatherViewModel.fetchWeatherData(lastLocation.getLatitude(), lastLocation.getLongitude());
                }
            } catch (SecurityException e) {
                Log.e(getString(R.string.weatherfragment), getString(R.string.security_exception) + e.getMessage());
            }
        } else {
            if (isAdded()) {
                Log.e(getString(R.string.weatherfragment), getString(R.string.network_provider_is_not_enabled));
                showLocationServicesDialog();
            }
        }
    }

    private void showLocationServicesDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.location_services_disabled)
                .setMessage(R.string.please_enable_location_services)
                .setPositiveButton(R.string.open_settings, (dialog, which) -> {
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void handlePermissionDenied() {
        if (isAdded()) {
            Log.e(getString(R.string.weatherfragment), getString(R.string.location_permissions_denied));
            Snackbar.make(requireView(), getString(R.string.location_permissions_denied), Snackbar.LENGTH_SHORT).show();
            openSettings();
        }
    }

    private void openSettings() {
        if (isAdded()) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", requireContext().getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        }
    }

    private void updateTemperatureDisplay(WeatherResponse weatherResponse) {
        boolean isCelsius = weatherViewModel.getIsCelsius().getValue() != null && weatherViewModel.getIsCelsius().getValue();
        float tempMax = weatherResponse.main.temp_max - 273.15f;
        float tempMin = weatherResponse.main.temp_min - 273.15f;

        if (!isCelsius) {
            tempMax = tempMax * 9/5 + 32;
            tempMin = tempMin * 9/5 + 32;
        }

        String tempMaxFormatted = String.format(getString(R.string.tempFormat), tempMax);
        String tempMinFormatted = String.format(getString(R.string.tempFormat), tempMin);

        tvHighTemp.setText(getString(R.string.high) + tempMaxFormatted + (isCelsius ? getString(R.string.celsius) : getString(R.string.fahrenheit)));
        tvLowTemp.setText(getString(R.string.low) + tempMinFormatted + (isCelsius ? getString(R.string.celsius) : getString(R.string.fahrenheit)));
    }
}