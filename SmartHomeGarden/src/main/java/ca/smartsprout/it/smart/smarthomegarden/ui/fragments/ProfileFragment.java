/**
 * Smart Sprout
 * Members:
 * 1. Aditi Patel, n01525570, CENG322-RCB
 * 2. Birava Prajapati, n01579924, CENG322-RCA
 * 3. Darshankumar Prajapati, n01574247, CENG322-RCB
 * 4. Zeel Patel, n01526282, CENG322-RCB
 */

package ca.smartsprout.it.smart.smarthomegarden.ui.fragments;

import static ca.smartsprout.it.smart.smarthomegarden.utils.Constants.KEY_USER_NAME;
import static ca.smartsprout.it.smart.smarthomegarden.utils.Constants.KEY_USER_PIC;
import static ca.smartsprout.it.smart.smarthomegarden.utils.Constants.PREFS_USER_PROFILE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.ui.AccountSettingsActivity;


public class ProfileFragment extends Fragment {


    boolean isOptionsVisible;
    private ImageView imageView;
    private TextView userNameTV;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;

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

        // Load initial user data from SharedPreferences
        loadUserProfile();

        // Set up the profile card click listener
        CardView profileCardView = view.findViewById(R.id.profileCardView);
        profileCardView.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AccountSettingsActivity.class);
            startActivity(intent);
        });
        // Set up SharedPreferences listener to update UI in real-time
        preferenceChangeListener = (sharedPreferences, key) -> {
            if (KEY_USER_NAME.equals(key) || KEY_USER_PIC.equals(key)) {
                loadUserProfile(); // Reload data if there’s a change
            }
        };
        sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceChangeListener);


        fabMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isOptionsVisible) {
                    isOptionsVisible = false;
                    fabAddPlant.animate().translationY(0);
                    fabAddPicture.animate().translationY(0);
                    fabAddTask.animate().translationY(0);

                } else {
                    isOptionsVisible = true;
                    fabAddPlant.animate().translationY(-getResources().getDimension(R.dimen.stan_60));
                    fabAddPicture.animate().translationY(-getResources().getDimension(R.dimen.stan_110));
                    fabAddTask.animate().translationY(-getResources().getDimension(R.dimen.stan_160));
                }

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
        String userName = sharedPreferences.getString(KEY_USER_NAME, "Your Name");
        userNameTV.setText(userName);

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