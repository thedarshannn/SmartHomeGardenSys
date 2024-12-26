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

public class PlantDetailsBottomSheetFragment extends BottomSheetDialogFragment {

    private PlantDetail plantDetail;

    public static PlantDetailsBottomSheetFragment newInstance(PlantDetail plantDetail) {
        PlantDetailsBottomSheetFragment fragment = new PlantDetailsBottomSheetFragment();
        Bundle args = new Bundle();
        args.putSerializable("plantDetail", plantDetail);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            plantDetail = (PlantDetail) getArguments().getSerializable("plantDetail");
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
        if (plantDetail != null) {
            tvPlantName.setText(plantDetail.getName() != null ? plantDetail.getName() : getString(R.string.name_not_available));
            tvPlantDescription.setText(plantDetail.getDescription() != null && plantDetail.getDescription().getValue() != null ? plantDetail.getDescription().getValue() : getString(R.string.description_not_available));

            if (plantDetail.getWatering() != null) {
                tvWatering.setText(plantDetail.getWatering().getMax() == 1 ? "Low Watering" : "Frequent Watering");
            } else {
                tvWatering.setText(R.string.watering_info_not_available);
            }

            if (plantDetail.getPropagationMethods() != null) {
                tvPropagation.setText(getString(R.string.propagation) + String.join(", ", plantDetail.getPropagationMethods()));
            } else {
                tvPropagation.setText(R.string.propagation_info_not_available);
            }

            if (plantDetail.getImage() != null) {
                Glide.with(requireContext())
                        .load(plantDetail.getImage().getValue())
                        .placeholder(R.drawable.image_placeholder)
                        .into(plantImage);
            }
        }

        // Set button click listener
        btnAddPlant.setOnClickListener(v -> {
            AddPlantBottomSheet fragment = AddPlantBottomSheet.newInstance(plantDetail.toPlant());
            fragment.show(getParentFragmentManager(), "PlantDetailsBottomSheet");
            dismiss();
        });

        return view;
    }
}
