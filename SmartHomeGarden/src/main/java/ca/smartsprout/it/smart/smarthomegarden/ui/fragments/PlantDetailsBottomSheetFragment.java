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

import java.io.Serializable;

import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.data.model.PlantDetail;

public class PlantDetailsBottomSheetFragment extends BottomSheetDialogFragment {

    private PlantDetail plantDetail;

    public static PlantDetailsBottomSheetFragment newInstance(PlantDetail plantDetail) {
        PlantDetailsBottomSheetFragment fragment = new PlantDetailsBottomSheetFragment();
        Bundle args = new Bundle();
        args.putSerializable("plantDetail", (Serializable) plantDetail);
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
            tvPlantName.setText(plantDetail.getName());
            tvPlantDescription.setText(plantDetail.getDescription().getValue());

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
            // Logic for adding plant
            dismiss();
        });

        return view;
    }
}
