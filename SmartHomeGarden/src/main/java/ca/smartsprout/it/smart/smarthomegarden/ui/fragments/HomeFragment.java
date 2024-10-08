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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextClock;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.data.model.WeatherResponse;
import ca.smartsprout.it.smart.smarthomegarden.data.repository.WeatherService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HomeFragment extends Fragment {

    private static final String API_KEY = "16f364b7879945df5f09f3f5b081fb6a";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";

    private TextView tvHighTemp, tvLowTemp;

    // Handle location permission result
    private final ActivityResultLauncher<String[]> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                Boolean fineLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false);
                Boolean coarseLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false);

                if (fineLocationGranted != null && fineLocationGranted) {
                    // Location permission was granted
                    fetchWeatherData(); // Only fetch weather data now, after permission is granted
                    Snackbar.make(requireView(), R.string.location_permission_granted, Snackbar.LENGTH_SHORT).show();
                } else if (coarseLocationGranted != null && coarseLocationGranted) {
                    // Location permission was granted
                    fetchWeatherData();
                    Snackbar.make(requireView(), R.string.location_permission_granted, Snackbar.LENGTH_SHORT).show();
                } else if (result.containsKey(Manifest.permission.ACCESS_FINE_LOCATION) || result.containsKey(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    // Location permission was denied
                    handlePermissionDenied();
                }
            });


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_home, container, false);


        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextClock clockTC = view.findViewById(R.id.textClock);
        clockTC.setFormat12Hour(getString(R.string.date_format));

        TextView greetingTextView = view.findViewById(R.id.greetingTextView);
        // Set the greeting with the username
        String username = getString(R.string.sir); // Gonna change this later with the username of the user
        greetingTextView.setText(getString(R.string.hello) + username);
        // Initialize UI elements
        tvHighTemp = view.findViewById(R.id.tv_high_temp);
        tvLowTemp = view.findViewById(R.id.tv_low_temp);

        // Check if location permissions are already granted
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request permissions, but do not show the Snackbar yet
            requestPermissionLauncher.launch(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION});
        } else {
            // Permissions are already granted, fetch weather data immediately
            fetchWeatherData();
        }
        return view;
    }

    private void fetchWeatherData() {
        // Ensure permissions are granted before accessing location
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return; // Permissions are not granted, so we stop
        }

        // Get the location manager
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        // Check if the location provider is enabled
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double lat = location.getLatitude();
                    double lon = location.getLongitude();

                    // Fetch weather data using Retrofit
                    getWeatherData(lat, lon);
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

    private void getWeatherData(double lat, double lon) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherService service = retrofit.create(WeatherService.class);
        Call<WeatherResponse> call = service.getWeather(lat, lon, API_KEY);
        call.enqueue(new Callback<WeatherResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (isAdded() && response.isSuccessful() && response.body() != null) {
                    WeatherResponse weather = response.body();
                    if (weather.main != null) {
                        float tempMaxCelsius = weather.main.temp_max - 273.15f;
                        float tempMinCelsius = weather.main.temp_min - 273.15f;

                        // Format the temperatures to 2 decimal places
                        String tempMaxFormatted = String.format(getString(R.string.tempFormat), tempMaxCelsius);
                        String tempMinFormatted = String.format(getString(R.string.tempFormat), tempMinCelsius);

                        // Update the UI with fetched weather data
                        tvHighTemp.setText(getString(R.string.high) + tempMaxFormatted + getString(R.string.celius));
                        tvLowTemp.setText(getString(R.string.low) + tempMinFormatted + getString(R.string.celius));
                    }
                } else {
                    if (isAdded()) {
                        Log.e(getString(R.string.weatherfragment), getString(R.string.response_unsuccessful_or_null));
                    }
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                if (isAdded()) {
                    Log.e(getString(R.string.weatherfragment), getString(R.string.api_call_failed) + t.getMessage());
                }
            }
        });
    }

    // Handle permission denied by showing Snackbar with "Settings" button
    private void handlePermissionDenied() {
        if (isAdded()){
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
}