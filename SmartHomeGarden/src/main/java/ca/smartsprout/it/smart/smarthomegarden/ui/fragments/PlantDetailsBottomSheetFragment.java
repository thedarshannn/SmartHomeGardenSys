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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.data.model.Plant;

public class PlantDetailsBottomSheetFragment extends BottomSheetDialogFragment {

    private static Plant plant;

    public static PlantDetailsBottomSheetFragment newInstance(Plant plantDetail) {
        PlantDetailsBottomSheetFragment fragment = new PlantDetailsBottomSheetFragment();
        Bundle args = new Bundle();
        args.putSerializable("Plant", plantDetail);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            plant = (Plant) getArguments().getSerializable("Plant");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plant_details_bottom_sheet_list_dialog_item, container, false);

        // Bind views
        ImageView plantImage = view.findViewById(R.id.ivPlantImage);
        TextView tvPlantName = view.findViewById(R.id.tvPlantName);
        TextView tvPlantDescription = view.findViewById(R.id.tvPlantDescription);
        TextView tvWatering = view.findViewById(R.id.tvWatering);
        TextView tvToxicity = view.findViewById(R.id.tvToxicity);
        TextView tvSuitability = view.findViewById(R.id.tvSuitability);
        Button btnAddPlant = view.findViewById(R.id.btnAddPlant);

        // Populate plant details
        if (plant != null) {
            // Set plant name
            tvPlantName.setText(plant.getName() != null ? plant.getName() : getString(R.string.name_not_available));

            // Set plant description
            tvPlantDescription.setText(plant.getDescription() != null ? plant.getDescription() : getString(R.string.description_not_available));

            // Set watering period
            tvWatering.setText(getString(R.string.watering_period_label,
                    plant.getWateringPeriod() != null ? plant.getWateringPeriod() : getString(R.string.watering_info_not_available)));

            // Set toxicity
            tvToxicity.setText(getString(R.string.toxicity_label,
                    plant.getToxicity() != null ? plant.getToxicity() : getString(R.string.toxicity_info_not_available)));

            // Set suitability
            tvSuitability.setText(getString(R.string.suitability_label,
                    plant.getSuitability() != null ? plant.getSuitability() : getString(R.string.suitability_info_not_available)));

            // Load image with Glide or a placeholder
            Glide.with(this)
                    .load(R.drawable.ic_sprout) // Replace with actual plant image URL if available
                    .placeholder(R.drawable.image_placeholder)
                    .into(plantImage);
        }

        // Set button click listener
        btnAddPlant.setOnClickListener(v -> {
            AddPlantBottomSheet fragment = AddPlantBottomSheet.newInstance(plant);
            fragment.show(getParentFragmentManager(), "PlantDetailsBottomSheet");
            dismiss();
        });

        return view;
    }
}
