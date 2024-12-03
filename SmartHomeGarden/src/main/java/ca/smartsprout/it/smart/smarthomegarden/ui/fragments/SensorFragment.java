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
        View view = inflater.inflate(R.layout.fragment_sensor, container, false);

        // Initialize UI elements
        sunlightValueTextView = view.findViewById(R.id.sunlight_value);
        temperatureValueTextView = view.findViewById(R.id.temperature_value);
        moistureValueTextView = view.findViewById(R.id.moisture_value);
        sunlightProgressBar = view.findViewById(R.id.sunlight_progress);
        temperatureProgressBar = view.findViewById(R.id.temperature_progress);
        moistureProgressBar = view.findViewById(R.id.moisture_progress);

        // Observe LiveData from ViewModel
        sensorViewModel.getSensorData().observe(getViewLifecycleOwner(), sensorData -> {
            if (sensorData != null) {
                sunlightValueTextView.setText(String.valueOf(sensorData.getSunlight()));
                sunlightProgressBar.setProgress((int) sensorData.getSunlight());

                temperatureValueTextView.setText(String.valueOf(sensorData.getTemperature()));
                temperatureProgressBar.setProgress((int) sensorData.getTemperature());

                moistureValueTextView.setText(String.valueOf(sensorData.getMoisture()));
                moistureProgressBar.setProgress((int) sensorData.getMoisture());
            }
        });

        return view;
    }

}
