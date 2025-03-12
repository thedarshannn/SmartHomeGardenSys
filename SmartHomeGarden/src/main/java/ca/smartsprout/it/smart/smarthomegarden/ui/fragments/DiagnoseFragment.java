package ca.smartsprout.it.smart.smarthomegarden.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.utils.NetworkUtils;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.NetworkViewModel;

public class DiagnoseFragment extends Fragment {

    private NetworkViewModel networkViewModel;
    private ViewGroup rootView;

    public DiagnoseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_diagnose, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize the root view (FrameLayout)
        rootView = (ViewGroup) view;

        // Initialize ViewModel
        networkViewModel = new ViewModelProvider(requireActivity()).get(NetworkViewModel.class);

        // Observe network status
        networkViewModel.getIsConnected().observe(getViewLifecycleOwner(), isConnected -> {
            if (isConnected) {
                // Device is online
                NetworkUtils.removeOfflineOverlay(rootView); // Remove offline overlay
            } else {
                // Device is offline
                NetworkUtils.showOfflineOverlay(requireContext(), rootView); // Show offline overlay
            }
        });
    }
}