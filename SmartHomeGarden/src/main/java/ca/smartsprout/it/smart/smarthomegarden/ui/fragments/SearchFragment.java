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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Objects;

import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.ui.adapter.PlantAdapter;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.PlantViewModel;


public class SearchFragment extends Fragment {

    private PlantViewModel plantViewModel;
    private PlantAdapter plantAdapter;


    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.searchRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        // Set up the adapter with an empty list initially
        plantAdapter = new PlantAdapter(new ArrayList<>());
        recyclerView.setAdapter(plantAdapter);

        // Set up ViewModel and observe LiveData
        plantViewModel = new ViewModelProvider(this).get(PlantViewModel.class);
        plantViewModel.getPlantList().observe(getViewLifecycleOwner(), plants -> {
            // Update the adapter's data when the plant list changes
            plantAdapter.updatePlantList(plants);
        });
        // Inflate the layout for this fragment
        return view;
    }
}