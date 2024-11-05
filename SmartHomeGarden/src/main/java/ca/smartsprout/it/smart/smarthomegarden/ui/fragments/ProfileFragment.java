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

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ca.smartsprout.it.smart.smarthomegarden.R;


public class ProfileFragment extends Fragment {

    boolean isOptionsVisible;

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
}