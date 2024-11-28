package ca.smartsprout.it.smart.smarthomegarden.ui.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.smartsprout.it.smart.smarthomegarden.databinding.FragmentPlantDetailsBottomSheetListDialogBinding;

public class PlantDetailsBottomSheetFragment extends BottomSheetDialogFragment {

    private FragmentPlantDetailsBottomSheetListDialogBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {



        binding = FragmentPlantDetailsBottomSheetListDialogBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}