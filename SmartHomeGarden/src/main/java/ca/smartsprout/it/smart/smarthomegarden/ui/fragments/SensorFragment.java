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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.data.model.Plant;
import ca.smartsprout.it.smart.smarthomegarden.data.repository.PlantRepository;
import ca.smartsprout.it.smart.smarthomegarden.utils.Util;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.SensorViewModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SensorFragment extends Fragment {

    private SensorViewModel sensorViewModel;
    private PlantRepository plantRepository;

    private TextView sunlightValueTextView, temperatureValueTextView, moistureValueTextView;
    private ProgressBar sunlightProgressBar, temperatureProgressBar, moistureProgressBar;
    private AutoCompleteTextView spinnerPlantSelection;

    private Map<String, String> plantNameToIdMap = new HashMap<>();

    public SensorFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        plantRepository = new PlantRepository();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sensor, container, false);

        sensorViewModel = new ViewModelProvider(this).get(SensorViewModel.class);

        // UI initialization
        sunlightValueTextView = view.findViewById(R.id.sunlight_value);
        temperatureValueTextView = view.findViewById(R.id.temperature_value);
        moistureValueTextView = view.findViewById(R.id.moisture_value);

        sunlightProgressBar = view.findViewById(R.id.sunlight_progress);
        temperatureProgressBar = view.findViewById(R.id.temperature_progress);
        moistureProgressBar = view.findViewById(R.id.moisture_progress);

        spinnerPlantSelection = view.findViewById(R.id.spinner_plant_selection);

        // Fetch plants and populate dropdown
        plantRepository.fetchPlants().observe(getViewLifecycleOwner(), plants -> {
            if (plants != null && !plants.isEmpty()) {
                List<String> plantNames = new ArrayList<>();
                for (Plant plant : plants) {
                    String name = plant.getCustomName();
                    plantNames.add(name);
                    plantNameToIdMap.put(name, plant.getId());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, plantNames);
                spinnerPlantSelection.setAdapter(adapter);

                // Default: Select first plant
//                String firstPlantId = plantNameToIdMap.get(plantNames.get(0));
//                initSensorViewModel(firstPlantId);
            }
        });

        // Handle plant selection
        spinnerPlantSelection.setOnItemClickListener((parent, view1, position, id) -> {
            String selectedName = parent.getItemAtPosition(position).toString();
            String plantId = plantNameToIdMap.get(selectedName);
            if (plantId != null) {
                showConfirmationDialog(plantNameToIdMap.get(selectedName));
            }
        });

        return view;
    }

    @SuppressLint("SetTextI18n")
    private void initSensorViewModel(String plantId) {
        sensorViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory())
                .get(SensorViewModel.class);

        sensorViewModel.setPlantId(plantId); // dynamic plant id

        sensorViewModel.getSensorData().observe(getViewLifecycleOwner(), sensorData -> {

            if (sensorData != null) {

                // uv
                String uvLevel = Util.getUVLevelDescription(sensorData.getUV());
                sunlightValueTextView.setText(uvLevel);
                sunlightProgressBar.setProgress((int) sensorData.getUV());


                int moisturePercent = Util.convertMoistureToPercentage(sensorData.getMoisture());
                moistureValueTextView.setText(moisturePercent + "%");
                moistureProgressBar.setProgress(moisturePercent);


                // Brightness (lux)
                float lux = sensorData.getLux();
                temperatureValueTextView.setText(lux + " lx");
                int luxPercent = (int) Math.min(100, (lux / 1000f) * 100); // convert to %
                temperatureProgressBar.setProgress(luxPercent);

            } else {
                // Handle null sensor
                temperatureValueTextView.setText("--lx");
                temperatureProgressBar.setProgress(0);

                moistureValueTextView.setText("--%");
                moistureProgressBar.setProgress(0);

                sunlightValueTextView.setText("--");
                sunlightProgressBar.setProgress(0);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void showConfirmationDialog(String plantId) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());
        builder.setIcon(R.drawable.ic_alert_sensor) // optional, match hero icon
                .setTitle("Did you move your sensor to the selected plant?")
                .setMessage("Selecting the correct plant ensures accurate readings.")
                .setPositiveButton("Yes", (dialog, which) -> {
                    sensorViewModel.updateSensorCommand(plantId);
                    initSensorViewModel(plantId); // trigger real reading
                })
                .setNegativeButton("No", (dialog, which) -> {
                    temperatureValueTextView.setText("--lx");
                    temperatureProgressBar.setProgress(0);

                    moistureValueTextView.setText("--%");
                    moistureProgressBar.setProgress(0);

                    sunlightValueTextView.setText("--");
                    sunlightProgressBar.setProgress(0);
                })
                .show();
    }

}


