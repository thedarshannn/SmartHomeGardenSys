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
import android.net.Uri;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;


import android.util.Log;
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
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.data.model.Photo;
import ca.smartsprout.it.smart.smarthomegarden.data.repository.PhotoRepository;
import ca.smartsprout.it.smart.smarthomegarden.ui.AccountSettingsActivity;
import ca.smartsprout.it.smart.smarthomegarden.ui.adapter.ProfilePlantAdapter;
import ca.smartsprout.it.smart.smarthomegarden.utils.GridSpacingDecoration;
import ca.smartsprout.it.smart.smarthomegarden.utils.ImagePickerHandler;
import ca.smartsprout.it.smart.smarthomegarden.utils.NetworkUtils;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.NetworkViewModel;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.PlantViewModel;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.UserViewModel;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.PhotoViewModel;
import ca.smartsprout.it.smart.smarthomegarden.ui.adapter.PhotoAdapter;


public class ProfileFragment extends Fragment {

    boolean isOptionsVisible;
    private ImageView imageView;
    private TextView userNameTV;
    private View addPlantContainer, cameraContainer, addTaskContainer;
    private UserViewModel userViewModel;
    private PhotoViewModel photosViewModel;
    private PlantViewModel plantViewModel;
    private ProfilePlantAdapter plantAdapter;
    private PhotoAdapter adapter;
    private RecyclerView photosGrid;
    private RecyclerView recyclerView;
    private TabLayout tabLayout;
    private ActivityResultLauncher<String> requestCameraPermissionLauncher;
    private ActivityResultLauncher<String> requestGalleryPermissionLauncher;
    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private String currentDate;
    private PhotoRepository photoRepository;


    public ProfileFragment() {

        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        photoRepository = new PhotoRepository(requireActivity().getApplication());
        currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // Initialize ViewModel
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        photosViewModel = new ViewModelProvider(requireActivity()).get(PhotoViewModel.class);
        userViewModel.getUserID().observe(this, userId -> {
            if (userId != null) {
                photosViewModel.fetchPhotosFromFirebase(userId);
            }
        });

        // Initialize camera permission launcher
        requestCameraPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        // Open the camera via ImagePickerHandler
                        ImagePickerHandler.openCamera((AppCompatActivity) requireActivity(), cameraLauncher);
                    } else {
                        Toast.makeText(requireContext(), "Camera permission denied.", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Initialize gallery permission launcher
        requestGalleryPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        // Open the gallery via ImagePickerHandler
                        ImagePickerHandler.openGallery((AppCompatActivity) requireActivity(), galleryLauncher);
                    } else {
                        Toast.makeText(requireContext(), "Gallery permission denied.", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Initialize camera launcher
                cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == AppCompatActivity.RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        if (imageUri != null) {
                            Photo photo = new Photo(imageUri.toString(), currentDate);
                            adapter.addPhoto(photo);
                        } else {
                            Toast.makeText(requireContext(), "Failed to capture image.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        // Initialize gallery launcher
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == AppCompatActivity.RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        if (imageUri != null) {
                            Photo photo = new Photo(imageUri.toString(), currentDate);
                            adapter.addPhoto(photo);
                            photoRepository.uploadImageToFirebase(imageUri, userViewModel.getUserId());
                        } else {
                            Toast.makeText(requireContext(), "Failed to select image.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        photosGrid = view.findViewById(R.id.photosGrid);

        tabLayout = view.findViewById(R.id.tabLayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        FloatingActionButton fabMain = view.findViewById(R.id.floatingActionButton);
        final FloatingActionButton fabAddPlant = view.findViewById(R.id.addplant);
        final FloatingActionButton fabAddPicture = view.findViewById(R.id.camera);
        final FloatingActionButton fabAddTask = view.findViewById(R.id.addtask);

        imageView = view.findViewById(R.id.imageView);
        userNameTV = view.findViewById(R.id.userNameTV);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        photosViewModel = new ViewModelProvider(requireActivity()).get(PhotoViewModel.class);

        photosGrid.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        photosGrid.addItemDecoration(new GridSpacingDecoration(14));
        photosViewModel.getAllPhotos().observe(getViewLifecycleOwner(), photos -> {
            adapter = new PhotoAdapter(requireContext(), photos, this);
            photosGrid.setAdapter(adapter);
        });


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

        // Initialize the root view (FrameLayout)
        ViewGroup rootView = (ViewGroup) view;

        // Initialize ViewModel
        NetworkViewModel networkViewModel = new ViewModelProvider(requireActivity()).get(NetworkViewModel.class);

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
                        galleryLauncher,
                        requestCameraPermissionLauncher,
                        requestGalleryPermissionLauncher
                );
            }
        });

        fabAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, new HomeFragment())
                        .addToBackStack(null)
                        .commit();

                // Update the bottom navigation indicator
                BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation);
                bottomNavigationView.setSelectedItemId(R.id.navigation_home);

                // Show the CustomBottomSheetFragment
                CustomBottomSheetFragment bottomSheetFragment = new CustomBottomSheetFragment();
                bottomSheetFragment.show(getParentFragmentManager(), bottomSheetFragment.getTag());
            }
        });

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        plantAdapter = new ProfilePlantAdapter(new ArrayList<>());
        recyclerView.setAdapter(plantAdapter);

        plantViewModel = new ViewModelProvider(requireActivity()).get(PlantViewModel.class);

        plantViewModel.getAllPlants().observe(getViewLifecycleOwner(), plants -> {
            if (plants != null) {
                plantAdapter.updatePlantList(plants);
            }
        });


        photosGrid.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        photosGrid.addItemDecoration(new GridSpacingDecoration(14));

        // Set up ViewModel
        photosViewModel.getAllPhotos().observe(getViewLifecycleOwner(), photos -> {
            adapter = new PhotoAdapter(requireContext(), photos, this);
            photosGrid.setAdapter(adapter);
        });


        // Handle Tab Selection
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) { // Plants Tab
                    recyclerView.setVisibility(View.VISIBLE);
                    photosGrid.setVisibility(View.GONE);
                } else if (tab.getPosition() == 1) { // Photos Tab
                    recyclerView.setVisibility(View.GONE);
                    photosGrid.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {// No action needed
            }
        });

        // Default to Plants Tab
        recyclerView.setVisibility(View.VISIBLE);
        photosGrid.setVisibility(View.GONE);
    }
           @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void deletePhoto(Photo photo) {
        if (photosViewModel != null) {
            photosViewModel.deletePhotoFromFirestore(photo);
            adapter.removePhoto(photo);
        } else {
            Log.e("ProfileFragment", "PhotoViewModel is not initialized");
        }
    }

}