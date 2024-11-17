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
import android.content.Context;
import android.content.Intent;
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

    // Handle location permission result
    private final ActivityResultLauncher<String[]> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                Boolean fineLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false);
                Boolean coarseLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false);

                if (fineLocationGranted != null && fineLocationGranted) {
                    fetchWeatherData();
                    Snackbar.make(requireView(), R.string.location_permission_granted, Snackbar.LENGTH_SHORT).show();
                } else if (coarseLocationGranted != null && coarseLocationGranted) {
                    fetchWeatherData();
                    Snackbar.make(requireView(), R.string.location_permission_granted, Snackbar.LENGTH_SHORT).show();
                } else {
                    handlePermissionDenied();
                }
            });

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize ViewModel
        weatherViewModel = new ViewModelProvider(this).get(WeatherViewModel.class);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        TextClock clockTC = view.findViewById(R.id.textClock);
        clockTC.setFormat12Hour(getString(R.string.date_format));
        weatherViewModel = new ViewModelProvider(this).get(WeatherViewModel.class);

        TextView greetingTextView = view.findViewById(R.id.greetingTextView);
        String username = getString(R.string.sir);
        greetingTextView.setText(getString(R.string.hello) + username);

        tvHighTemp = view.findViewById(R.id.tv_high_temp);
        tvLowTemp = view.findViewById(R.id.tv_low_temp);
        CardView weatherCardView = view.findViewById(R.id.cardView);

        Button buttonAdd = view.findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(v -> {
            // Navigate to SearchFragment
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, new SearchFragment())
                    .addToBackStack(null)
                    .commit();

            // Update bottom navigation indicator
            BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation);
            bottomNavigationView.setSelectedItemId(R.id.navigation_search);
        });

        weatherViewModel.getWeatherData().observe(getViewLifecycleOwner(), weatherResponse -> {
            if (weatherResponse != null && weatherResponse.main != null) {
                updateTemperatureDisplay(weatherResponse);
            }
        });

        // Add the observer for isCelsius here
        weatherViewModel.getIsCelsius().observe(getViewLifecycleOwner(), isCelsius -> {
            if (weatherViewModel.getWeatherData().getValue() != null) {
                updateTemperatureDisplay(weatherViewModel.getWeatherData().getValue());
            }
        });

        // Fetch weather data automatically when the fragment is created
        fetchWeatherData();

        // Set onClickListener on the weatherCardView
        weatherCardView.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                });
            } else {
                fetchWeatherData();
            }
        });

        return view;
    }

    private void fetchWeatherData() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double lat = location.getLatitude();
                    double lon = location.getLongitude();
                    weatherViewModel.fetchWeatherData(lat, lon);
                }

                @Override
                public void onProviderEnabled(String provider) {}

                @Override
                public void onProviderDisabled(String provider) {
                    if (isAdded()) {
                        Log.e(getString(R.string.weatherfragment), getString(R.string.location_provider_disabled));
                    }
                }
            });
        } else {
            if (isAdded()) {
                Log.e(getString(R.string.weatherfragment), getString(R.string.network_provider_is_not_enabled));
            }
        }
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
