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
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextClock;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import ca.smartsprout.it.smart.smarthomegarden.R;

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

                    Snackbar.make(requireView(), "Location permission granted", Snackbar.LENGTH_SHORT).show();
                } else if (coarseLocationGranted != null && coarseLocationGranted) {
                    // Location permission was granted

                    Snackbar.make(requireView(), "Location permission granted", Snackbar.LENGTH_SHORT).show();
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

        tvHighTemp = view.findViewById(R.id.tv_high_temp);
        tvLowTemp = view.findViewById(R.id.tv_low_temp);

        // Check if location permissions are already granted
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request permissions, but do not show the Snackbar yet
            requestPermissionLauncher.launch(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION});
        } else {
            // Permissions are already granted, fetch weather data immediately

        }
        return view;
    }
    private void handlePermissionDenied() {
        Log.e("WeatherFragment", "Location permissions denied.");
        Snackbar.make(requireView(), "Location permission denied", Snackbar.LENGTH_SHORT).show();
    }
}