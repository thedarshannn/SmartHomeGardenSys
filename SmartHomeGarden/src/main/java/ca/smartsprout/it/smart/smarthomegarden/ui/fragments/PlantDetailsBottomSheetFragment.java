package ca.smartsprout.it.smart.smarthomegarden.ui.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.databinding.FragmentPlantDetailsBottomSheetListDialogItemBinding;
import ca.smartsprout.it.smart.smarthomegarden.databinding.FragmentPlantDetailsBottomSheetListDialogBinding;

public class PlantDetailsBottomSheetFragment extends BottomSheetDialogFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

      View view = inflater.inflate(R.layout.fragment_plant_details_bottom_sheet_list_dialog, container, false);
      return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        final TextView text;

        ViewHolder(FragmentPlantDetailsBottomSheetListDialogItemBinding binding) {
            super(binding.getRoot());
            text = binding.text;
        }

    }
}