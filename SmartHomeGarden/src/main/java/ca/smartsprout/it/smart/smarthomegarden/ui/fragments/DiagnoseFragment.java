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

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.smartsprout.it.smart.smarthomegarden.R;


import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import ca.smartsprout.it.smart.smarthomegarden.viewmodels.PumpStateViewModel;

public class DiagnoseFragment extends Fragment {

    private TextView waterLevelText;
    private MaterialSwitch pumpToggle;
    private PumpStateViewModel pumpStateViewModel;

    private String userId;
    public DiagnoseFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pumpStateViewModel = new ViewModelProvider(this).get(PumpStateViewModel.class);
        userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diagnose, container, false);

        waterLevelText = view.findViewById(R.id.waterLevelText);
        pumpToggle = view.findViewById(R.id.pumpToggle);

        // Observe Water Level
        pumpStateViewModel.getWaterLevel(userId).observe(getViewLifecycleOwner(), level -> {
            waterLevelText.setText(level + "%");
        });

        // Sync Pump Toggle State
        pumpStateViewModel.getPumpState(userId).observe(getViewLifecycleOwner(), state -> {
            pumpToggle.setChecked("on".equals(state));
        });

        // Toggle listener to update relay state
        pumpToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String newState = isChecked ? "on" : "off";
            pumpStateViewModel.updatePumpState(userId, newState);
        });

        return view;
    }
}
