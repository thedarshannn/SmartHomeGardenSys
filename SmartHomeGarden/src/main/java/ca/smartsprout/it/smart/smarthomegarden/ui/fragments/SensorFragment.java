/**
 * Smart Sprout
 * Members:
 * 1. Aditi Patel, n01525570, CENG322-RCB
 * 2. Birava Prajapati, n01579924, CENG322-RCA
 * 3. Darshankumar Prajapati, n01574247, CENG322-RCB
 * 4. Zeel Patel, n01526282, CENG322-RCB
 */

package ca.smartsprout.it.smart.smarthomegarden.ui.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log; // Import Log for debugging
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;
import java.util.Map;

import ca.smartsprout.it.smart.smarthomegarden.R;

public class SensorFragment extends Fragment {

    private DatabaseReference mDatabase;
    private TextView sunlightValueTextView;
    private TextView temperatureValueTextView;
    private TextView moistureValueTextView;
    private ProgressBar sunlightProgressBar;
    private ProgressBar temperatureProgressBar;
    private ProgressBar moistureProgressBar;

    public SensorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sensor, container, false);

        // Initialize UI elements
        sunlightValueTextView = view.findViewById(R.id.sunlight_value);
        temperatureValueTextView = view.findViewById(R.id.temperature_value);
        moistureValueTextView = view.findViewById(R.id.moisture_value);
        sunlightProgressBar = view.findViewById(R.id.sunlight_progress);
        temperatureProgressBar = view.findViewById(R.id.temperature_progress);
        moistureProgressBar = view.findViewById(R.id.moisture_progress);

        // Update sensor readings (for demonstration purposes)
        updateSensorReadings(57.0, 40.5, 60.2);

        // Fetch and display sensor readings from the database
        fetchSensorReadings();

        return view;
    }

    private void updateSensorReadings(double sunlight, double temperature, double moisture) {
        // Create a map to store sensor readings
        Map<String, Double> sensorReadings = new HashMap<>();
        sensorReadings.put(getString(R.string.sunlight), sunlight);
        sensorReadings.put(getString(R.string.temperature), temperature);
        sensorReadings.put(getString(R.string.moisture), moisture);

        // Update the database
        mDatabase.child(getString(R.string.sensors)).setValue(sensorReadings);
    }

    private void fetchSensorReadings() {
        mDatabase.child(getString(R.string.sensors)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Double sunlight = dataSnapshot.child(getString(R.string.sunlight)).getValue(Double.class);
                    Double temperature = dataSnapshot.child(getString(R.string.temperature)).getValue(Double.class);
                    Double moisture = dataSnapshot.child(getString(R.string.moisture)).getValue(Double.class);

                    // Log the fetched values
                    Log.d(("sensorfragment"), "Fetched values - Sunlight:"  + sunlight +  "Temperature: " + temperature +"  Moisture: " + moisture);

                    // Update the UI with the fetched data
                    if (sunlight != null) {
                        sunlightValueTextView.setText(String.valueOf(sunlight));
                        sunlightProgressBar.setProgress(sunlight.intValue());
                    }

                    if (temperature != null) {
                        temperatureValueTextView.setText(String.valueOf(temperature));
                        temperatureProgressBar.setProgress(temperature.intValue());
                    }

                    if (moisture != null) {
                        moistureValueTextView.setText(String.valueOf(moisture));
                        moistureProgressBar.setProgress(moisture.intValue());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
                Log.e("SensorFragment", "Database error: " + databaseError.getMessage());
            }
        });
    }
}
