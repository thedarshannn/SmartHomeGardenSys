/**
 * Smart Sprout
 * Members:
 * 1. Aditi Patel, n01525570, CENG322-RCB
 * 2. Birava Prajapati, n01579924, CENG322-RCA
 * 3. Darshankumar Prajapati, n01574247, CENG322-RCB
 * 4. Zeel Patel, n01526282, CENG322-RCB
 */

package ca.smartsprout.it.smart.smarthomegarden.ui.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.ui.AccountSettingsActivity;
import ca.smartsprout.it.smart.smarthomegarden.utils.ImagePickerHandler;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.UserViewModel;


public class ProfileFragment extends Fragment {

     private final ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == requireActivity().RESULT_OK && result.getData() != null) {
                    // Handle the image captured by the camera
                    Toast.makeText(requireContext(), "Camera image captured.", Toast.LENGTH_SHORT).show();
                }
            });

    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == requireActivity().RESULT_OK && result.getData() != null) {
                    // Handle the image selected from the gallery
                    Toast.makeText(requireContext(), "Gallery image selected.", Toast.LENGTH_SHORT).show();
                }
            });

    boolean isOptionsVisible;
    private ImageView imageView;
    private TextView userNameTV;
    private View addPlantContainer, cameraContainer, addTaskContainer;
    private UserViewModel userViewModel;
    private PhotosViewModel photosViewModel;
    private PhotosAdapter adapter;

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
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        userViewModel.getUserName().observe(getViewLifecycleOwner(), name -> userNameTV.setText(name));
        // load profile picture and load with glide and update the image view from firebase
        userViewModel.getProfilePictureUrl().observe(getViewLifecycleOwner(), profilePictureUrl -> {
            if (profilePictureUrl != null) {
                Glide.with(requireActivity())
                        .load(profilePictureUrl)
                        .placeholder(R.drawable.user)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imageView);
            } else {
                // Clear the image if the URL is null
                imageView.setImageResource(R.drawable.user);
            }
        });

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
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, new SearchFragment()) // Ensure 'R.id.nav_host_fragment' is the container ID
                        .addToBackStack(null) // Add to back stack for navigation
                        .commit();

                // Update the bottom navigation indicator
                BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation);
                bottomNavigationView.setSelectedItemId(R.id.navigation_search);
            }
        });

        fabAddPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle adding a picture of a plant
                ImagePickerHandler.showImagePickerDialog(
                        (AppCompatActivity) requireActivity(),
                        cameraLauncher,
                        galleryLauncher
                );
            }
        });

        fabAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle adding a task for the plant
            }
        });

        // Initialize RecyclerView
        RecyclerView photosGrid = view.findViewById(R.id.photosGrid);
        photosGrid.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set up ViewModel
        photosViewModel = new ViewModelProvider(this).get(PhotosViewModel.class);

        // Observe LiveData and update RecyclerView
        photosViewModel.getPhotos().observe(getViewLifecycleOwner(), new Observer<List<ContactsContract.Contacts.Photo>>() {
            @Override
            public void onChanged(List<ContactsContract.Contacts.Photo> photos) {
                adapter = new PhotosAdapter(requireContext(), photos);
                photosGrid.setAdapter(adapter);
            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}