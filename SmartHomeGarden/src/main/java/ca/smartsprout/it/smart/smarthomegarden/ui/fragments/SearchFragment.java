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
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.ui.adapter.PlantAdapter;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.PlantViewModel;

public class SearchFragment extends Fragment {

    private PlantAdapter plantAdapter;
    private PlantViewModel plantViewModel;
    private TextView noResultsTextView;
    private ExecutorService executorService;
    private Handler mainHandler;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // Initialize ExecutorService (fixed thread pool)
        executorService = Executors.newFixedThreadPool(2);

        // Initialize Handler for UI updates
        mainHandler = new Handler(Looper.getMainLooper());

        // Initialize RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.searchRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        plantAdapter = new PlantAdapter(new ArrayList<>());
        recyclerView.setAdapter(plantAdapter);

        // Initialize TextView for empty state
        noResultsTextView = view.findViewById(R.id.noResultsTextView);

        // Initialize ViewModel
        plantViewModel = new ViewModelProvider(this).get(PlantViewModel.class);

        // Observe LiveData for plant list updates
        plantViewModel.getPlantList().observe(getViewLifecycleOwner(), plants -> {
            executorService.submit(() -> mainHandler.post(() -> {
                if (plants != null && !plants.isEmpty()) {
                    noResultsTextView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    plantAdapter.updatePlantList(plants);
                } else {
                    noResultsTextView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }));
        });

        // Initialize SearchView
        SearchView searchView = view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                executorService.submit(() -> plantViewModel.searchPlants(query)); // Perform search in a thread
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    executorService.submit(() -> plantViewModel.loadPlants()); // Load default plants in a thread
                }
                return true;
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        executorService.shutdown(); // Gracefully shut down ExecutorService
    }
}
