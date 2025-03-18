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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.RelayViewModel;


public class DiagnoseFragment extends Fragment {
    private Switch relaySwitch;
    private RelayViewModel relayViewModel;

    public DiagnoseFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_diagnose, container, false);

        // Initialize switch and ViewModel
        relaySwitch = view.findViewById(R.id.relaySwitch);
        relayViewModel = new ViewModelProvider(this).get(RelayViewModel.class);

        // Observe relay state changes from ViewModel
        relayViewModel.getRelayState().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String state) {
                relaySwitch.setOnCheckedChangeListener(null); // Avoid triggering listener while updating UI
                relaySwitch.setChecked(state.equals("on"));
                relaySwitch.setOnCheckedChangeListener(switchListener);
            }
        });

        // Set switch listener to update Firebase when toggled
        relaySwitch.setOnCheckedChangeListener(switchListener);

        return view;
    }

    // Switch listener to update relay state in Firebase
    private final CompoundButton.OnCheckedChangeListener switchListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            String newState = isChecked ? "on" : "off";
            relayViewModel.updateRelayState(newState);
            Toast.makeText(getContext(), "Relay " + newState.toUpperCase(), Toast.LENGTH_SHORT).show();
        }
    };
}