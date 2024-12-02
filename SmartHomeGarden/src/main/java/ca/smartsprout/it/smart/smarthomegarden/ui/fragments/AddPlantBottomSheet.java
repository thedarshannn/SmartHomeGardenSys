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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import java.io.Serializable;
import java.util.Date;

import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.data.model.Plant;
import ca.smartsprout.it.smart.smarthomegarden.data.repository.PlantRepository;

public class AddPlantBottomSheet extends BottomSheetDialogFragment {

    private static final String PLANT_KEY = "plant";

    private TextInputEditText customNameEditText;
    private TextInputEditText specialNotesEditText;
    private MaterialTextView dateAddedTextView, titleTextView;
    private MaterialButton savePlantButton;

    private Plant selectedPlant; // Plant passed from the search result
    private PlantRepository plantRepository;

    public static AddPlantBottomSheet newInstance(Plant plant) {
        AddPlantBottomSheet fragment = new AddPlantBottomSheet();
        Bundle args = new Bundle();
        args.putSerializable(PLANT_KEY, plant);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_plant, container, false);

        // Initialize Views
        customNameEditText = view.findViewById(R.id.plantCustomName);
        specialNotesEditText = view.findViewById(R.id.specialNotes);
        dateAddedTextView = view.findViewById(R.id.dateAdded);
        savePlantButton = view.findViewById(R.id.savePlantButton);
        titleTextView = view.findViewById(R.id.titleAddPlant);

        // Initialize Repository
        plantRepository = new PlantRepository();

        // Retrieve Plant object from arguments
        if (getArguments() != null) {
            selectedPlant = (Plant) getArguments().getSerializable(PLANT_KEY);
            if (selectedPlant != null) {
                titleTextView.setText(String.format("%s%s", getString(R.string.adding), selectedPlant.getName()));
            } else {
                handleException(getString(R.string.error_plant_details_not_found));
                dismiss();
            }
        } else {
            handleException(getString(R.string.error_no_plant_details_passed));
            dismiss();
        }

        // Set the current date
        Date currentDate = new Date();
        dateAddedTextView.setText(currentDate.toString());

        // Save Button Logic
        savePlantButton.setOnClickListener(v -> {
            try {
                String customName = customNameEditText.getText().toString().trim();
                String specialNotes = specialNotesEditText.getText().toString().trim();

                if (TextUtils.isEmpty(customName)) {
                    Toast.makeText(getContext(), "Custom name cannot be empty.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Use existing plant details to save
                selectedPlant.setCustomName(customName);
                selectedPlant.setDateAdded(currentDate);
                selectedPlant.setDescription(specialNotes);

                plantRepository.addPlant(
                        selectedPlant.getName(),
                        customName,
                        currentDate,
                        specialNotes,
                        null,
                        new PlantRepository.OnPlantAddedListener() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(getContext(), R.string.plant_added_successfully, Toast.LENGTH_SHORT).show();
                                dismiss();
                            }

                            @Override
                            public void onFailure(Exception e) {
                                handleException(getString(R.string.error_adding_plant_please_try_again));
                            }
                        });
            } catch (Exception e) {
                handleException(getString(R.string.an_unexpected_error_occurred) + e.getMessage());
            }
        });

        return view;
    }

    private void handleException(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        dismiss();
    }
}
