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

    private static Plant Plant;

    public static PlantDetailsBottomSheetFragment newInstance(Plant plantDetail) {
        PlantDetailsBottomSheetFragment fragment = new PlantDetailsBottomSheetFragment();
        Bundle args = new Bundle();
        args.putSerializable("Plant", Plant);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Plant = (Plant) getArguments().getSerializable("Plant");
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
        TextView tvPropagation = view.findViewById(R.id.tvPropagation);
        Button btnAddPlant = view.findViewById(R.id.btnAddPlant);

        // Populate plant details
        if (Plant != null) {
            tvPlantName.setText(Plant.getName() != null ? Plant.getName() : getString(R.string.name_not_available));
            tvPlantDescription.setText(Plant.getDescription() != null ? Plant.getDescription() : getString(R.string.description_not_available));

            if (Plant.getWateringPeriod() != null) {
                tvWatering.setText(Plant.getWateringPeriod());
            } else {
                tvWatering.setText(R.string.watering_info_not_available);
            }

        }

        // Set button click listener
        btnAddPlant.setOnClickListener(v -> {
            AddPlantBottomSheet fragment = AddPlantBottomSheet.newInstance(Plant);
            fragment.show(getParentFragmentManager(), "PlantDetailsBottomSheet");
            dismiss();
        });

        return view;
    }
}
