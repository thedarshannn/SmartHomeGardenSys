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
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

import java.util.ArrayList;

import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.data.model.WeatherResponse;
import ca.smartsprout.it.smart.smarthomegarden.ui.adapter.PlantTaskAdapter;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.PlantTaskViewModel;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.PlantViewModel;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.UserViewModel;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.WeatherViewModel;
import ca.smartsprout.it.smart.smarthomegarden.data.repository.PlantRepository;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.TaskHistoryViewModel;

public class HomeFragment extends Fragment {

    private WeatherViewModel weatherViewModel;
    private TextView tvHighTemp, tvLowTemp;

    private static final String PERMISSION_PREFS = "permission_preferences";
    private static final String PERMISSION_GRANTED_KEY = "permission_granted";
    FragmentManager fragmentManager;
    private SwipeRefreshLayout swipeRefreshLayout;

    private UserViewModel userViewModel;
    private PlantTaskViewModel viewModel;
    private PlantTaskAdapter adapter;
    private RecyclerView recyclerView;
    private PlantViewModel viewModel1;
    private Button buttonAdd;
    private PlantRepository plantRepository;
    private TaskHistoryViewModel taskHistoryViewModel; // Add TaskHistoryViewModel

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

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weatherViewModel = new ViewModelProvider(this).get(WeatherViewModel.class);
        viewModel = new ViewModelProvider(this).get(PlantTaskViewModel.class);
        taskHistoryViewModel = new ViewModelProvider(this).get(TaskHistoryViewModel.class); // Initialize TaskHistoryViewModel

        // If permission was previously granted, start updates
        if (isPermissionPreviouslyGranted()) {
            startLocationUpdates();
        }
    }

    @SuppressLint({"SetTextI18n", "MissingInflatedId"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        TextClock clockTC = view.findViewById(R.id.textClock);
        clockTC.setFormat12Hour(getString(R.string.date_format));

        TextView greetingTextView = view.findViewById(R.id.greetingTextView);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        userViewModel.getUserName().observe(getViewLifecycleOwner(), name -> greetingTextView.setText(getString(R.string.hello) + name));

        tvHighTemp = view.findViewById(R.id.tv_high_temp);
        tvLowTemp = view.findViewById(R.id.tv_low_temp);
        swipeRefreshLayout = view.findViewById(R.id.swipe);

        recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new PlantTaskAdapter(getContext(), new ArrayList<>(), taskHistoryViewModel); // Pass TaskHistoryViewModel
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel.getTasks().observe(getViewLifecycleOwner(), tasks -> {
            adapter.updateTasks(tasks);
            adapter.notifyDataSetChanged();
        });

        // Add listener to the checkbox
        adapter.setOnCheckedChangeListener((task, isChecked) -> {
            if (isChecked) {
                viewModel.removeTask(task);
                adapter.removeTask(task);
            }
        });

        adapter.setOnEditButtonClickListener(task -> {
            CustomBottomSheetFragment bottomSheetFragment = new CustomBottomSheetFragment();

            // Pass the task to the bottom sheet fragment
            Bundle args = new Bundle();
            args.putSerializable("task", task);
            bottomSheetFragment.setArguments(args);

            bottomSheetFragment.show(getParentFragmentManager(), bottomSheetFragment.getTag());
        });

        viewModel.getTasks().observe(getViewLifecycleOwner(), tasks -> adapter.notifyDataSetChanged());

        swipeRefreshLayout.setOnRefreshListener(() -> {
            // Check if conditions are met for refreshing (permission granted and location enabled)
            if (isPermissionPreviouslyGranted() && isLocationEnabled()) {
                // Reload the HomeFragment by replacing it with itself
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, new HomeFragment()); // Replace the current fragment
                transaction.addToBackStack(null); // Optionally add to back stack if you want to preserve navigation
                transaction.commit();

                // Stop the swipe refresh animation
                swipeRefreshLayout.setRefreshing(false);
            } else {
                swipeRefreshLayout.setRefreshing(false); // Stop refreshing if conditions aren't met
            }
        });

        CardView weatherCardView = view.findViewById(R.id.cardView);
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

        // Initialize ViewModel and Repository
        viewModel1 = new ViewModelProvider(this).get(PlantViewModel.class);
        plantRepository = new PlantRepository(); // Initialize your repository

        // Find the button
        buttonAdd = view.findViewById(R.id.buttonAdd);

        // Fetch plants and set the initial button state
        fetchPlantsAndUpdateButton();

        // Set the button's click listener
        buttonAdd.setOnClickListener(v -> {
            if ("Add Plant".equals(buttonAdd.getText().toString())) {
                // Navigate to the SearchFragment
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, new SearchFragment())
                        .addToBackStack(null)
                        .commit();

                // Update the BottomNavigationView
                BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation);
                bottomNavigationView.setSelectedItemId(R.id.navigation_search);
            } else if ("Add Task".equals(buttonAdd.getText().toString())) {
                // Show the CustomBottomSheetFragment
                CustomBottomSheetFragment bottomSheetFragment = new CustomBottomSheetFragment();
                bottomSheetFragment.show(getParentFragmentManager(), bottomSheetFragment.getTag());
            }
        });

        weatherViewModel.getWeatherData().observe(getViewLifecycleOwner(), weatherResponse -> {
            if (weatherResponse != null && weatherResponse.main != null) {
                updateTemperatureDisplay(weatherResponse);
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

        if (locationManager == null) {
            Log.e(getString(R.string.weatherfragment), getString(R.string.location_service_not_available));
            return;
        }

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
        tvHighTemp.setText(formatTemperature(weatherResponse.main.temp_max, isCelsius, getString(R.string.high)));
        tvLowTemp.setText(formatTemperature(weatherResponse.main.temp_min, isCelsius, getString(R.string.low)));
    }

    private String formatTemperature(float kelvinTemp, boolean isCelsius, String label) {
        float temp = kelvinTemp - 273.15f;
        if (!isCelsius) {
            temp = temp * 9 / 5 + 32;
        }
        return label + String.format(getString(R.string.tempFormat), temp) + (isCelsius ? getString(R.string.celsius) : getString(R.string.fahrenheit));
    }

    /**
     * Fetch plants from the repository and update the button state.
     */
    private void fetchPlantsAndUpdateButton() {
        plantRepository.fetchPlants().observe(getViewLifecycleOwner(), plants -> {
            if (plants != null && !plants.isEmpty()) {
                // If there are plants, set the button text to "Add Task"
                buttonAdd.setText("Add Task");
            } else {
                // If there are no plants, set the button text to "Add Plant"
                buttonAdd.setText("Add Plant");
            }
        });
    }
}