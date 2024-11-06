package ca.smartsprout.it.smart.smarthomegarden.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.data.model.SensorData;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.SensorViewModel;

public class SensorFragment extends Fragment {

    private SensorViewModel sensorViewModel;
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
        sensorViewModel = new ViewModelProvider(this).get(SensorViewModel.class);
    }

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

        // Set hard-coded sensor data directly
        SensorData hardCodedSensorData = new SensorData(89.97, 27.0, 4.90); // Example hard-coded values
        sensorViewModel.updateSensorData(hardCodedSensorData); // Send hard-coded data to database

        // Update UI components with hard-coded values
        sunlightValueTextView.setText(String.valueOf(hardCodedSensorData.getSunlight()));
        sunlightProgressBar.setProgress((int) hardCodedSensorData.getSunlight());

        temperatureValueTextView.setText(String.valueOf(hardCodedSensorData.getTemperature()));
        temperatureProgressBar.setProgress((int) hardCodedSensorData.getTemperature());

        moistureValueTextView.setText(String.valueOf(hardCodedSensorData.getMoisture()));
        moistureProgressBar.setProgress((int) hardCodedSensorData.getMoisture());

        return view;
    }
}
