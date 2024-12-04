/**
 * Smart Sprout
 * Members:
 * 1. Aditi Patel, n01525570, CENG322-RCB
 * 2. Birava Prajapati, n01579924, CENG322-RCA
 * 3. Darshankumar Prajapati, n01574247, CENG322-RCB
 * 4. Zeel Patel, n01526282, CENG322-RCB
 */

package ca.smartsprout.it.smart.smarthomegarden.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.data.model.Plant;
import ca.smartsprout.it.smart.smarthomegarden.data.repository.PlantRepository;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.SensorViewModel;
import java.util.ArrayList;
import java.util.List;

public class SensorFragment extends Fragment {

    private SensorViewModel sensorViewModel;
    private TextView sunlightValueTextView;
    private TextView temperatureValueTextView;
    private TextView moistureValueTextView;
    private ProgressBar sunlightProgressBar;
    private ProgressBar temperatureProgressBar;
    private ProgressBar moistureProgressBar;
    private AutoCompleteTextView spinnerPlantSelection;
    private PlantRepository plantRepository;

    public SensorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sensorViewModel = new ViewModelProvider(this).get(SensorViewModel.class);
        plantRepository = new PlantRepository();
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
        spinnerPlantSelection = view.findViewById(R.id.spinner_plant_selection);

        // Fetch plants and set up the spinner
        plantRepository.fetchPlants().observe(getViewLifecycleOwner(), plants -> {
            if (plants != null) {
                List<String> plantNames = new ArrayList<>();
                for (Plant plant : plants) {
                    plantNames.add(plant.getCustomName()); // Show custom name
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, plantNames);
                spinnerPlantSelection.setAdapter(adapter);
            }
        });

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

