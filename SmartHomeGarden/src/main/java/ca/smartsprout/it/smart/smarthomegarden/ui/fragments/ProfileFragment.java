/**
 * Smart Sprout
 * Members:
 * 1. Aditi Patel, n01525570, CENG322-RCB
 * 2. Birava Prajapati, n01579924, CENG322-RCA
 * 3. Darshankumar Prajapati, n01574247, CENG322-RCB
 * 4. Zeel Patel, n01526282, CENG322-RCB
 */

package ca.smartsprout.it.smart.smarthomegarden.ui.fragments;

import static ca.smartsprout.it.smart.smarthomegarden.utils.Constants.KEY_USER_PIC;
import static ca.smartsprout.it.smart.smarthomegarden.utils.Constants.PREFS_USER_PROFILE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.ui.AccountSettingsActivity;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.UserViewModel;


public class ProfileFragment extends Fragment {


    boolean isOptionsVisible;
    private ImageView imageView;
    private TextView userNameTV;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;
    private View addPlantContainer, cameraContainer, addTaskContainer;
    private UserViewModel userViewModel;

    public ProfileFragment() {

        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        FloatingActionButton fabMain = view.findViewById(R.id.floatingActionButton);
        final FloatingActionButton fabAddPlant = view.findViewById(R.id.addplant);
        final FloatingActionButton fabAddPicture = view.findViewById(R.id.camera);
        final FloatingActionButton fabAddTask = view.findViewById(R.id.addtask);

        imageView = view.findViewById(R.id.imageView);
        userNameTV = view.findViewById(R.id.userNameTV);
        sharedPreferences = requireContext().getSharedPreferences(PREFS_USER_PROFILE, Context.MODE_PRIVATE);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        userViewModel.getUserName().observe(getViewLifecycleOwner(), name -> userNameTV.setText(name));
        // Load initial user data from SharedPreferences
        loadUserProfile();

        // Set up the profile card click listener
        CardView profileCardView = view.findViewById(R.id.profileCardView);
        profileCardView.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AccountSettingsActivity.class);
            startActivity(intent);
        });


        fabMain = view.findViewById(R.id.floatingActionButton);
        addPlantContainer = view.findViewById(R.id.addplantContainer);
        cameraContainer = view.findViewById(R.id.cameraContainer);
        addTaskContainer = view.findViewById(R.id.addtaskContainer);

        // Toggle FAB menu with translationY animation based on your example
        fabMain.setOnClickListener(v -> {
            if (isOptionsVisible) {
                // Animate down and set visibility to GONE
                addPlantContainer.animate().translationY(0).withEndAction(() -> addPlantContainer.setVisibility(View.GONE));
                cameraContainer.animate().translationY(0).withEndAction(() -> cameraContainer.setVisibility(View.GONE));
                addTaskContainer.animate().translationY(0).withEndAction(() -> addTaskContainer.setVisibility(View.GONE));
                isOptionsVisible = false;
            } else {
                // Set visibility to VISIBLE and animate up
                addPlantContainer.setVisibility(View.VISIBLE);
                cameraContainer.setVisibility(View.VISIBLE);
                addTaskContainer.setVisibility(View.VISIBLE);

                addPlantContainer.animate().translationY(-getResources().getDimension(R.dimen.stan_60));
                cameraContainer.animate().translationY(-getResources().getDimension(R.dimen.stan_110));
                addTaskContainer.animate().translationY(-getResources().getDimension(R.dimen.stan_160));
                isOptionsVisible = true;
            }
        });

        fabAddPlant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle adding a new plant
            }
        });

        fabAddPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle adding a picture of a plant
            }
        });

        fabAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle adding a task for the plant
            }
        });

        return view;
    }

    private void loadUserProfile() {
        String uriString = sharedPreferences.getString(KEY_USER_PIC, null);
        if (uriString != null) {
            Uri uri = Uri.parse(uriString);
            imageView.setImageURI(uri); // Load URI directly into CircleImageView
        } else {
            imageView.setImageResource(R.drawable.user); // Default image
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
    }
}