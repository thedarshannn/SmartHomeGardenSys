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
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
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
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.data.model.Photo;
import ca.smartsprout.it.smart.smarthomegarden.data.model.Plant;
import ca.smartsprout.it.smart.smarthomegarden.data.repository.PhotoRepository;
import ca.smartsprout.it.smart.smarthomegarden.data.repository.PlantRepository;
import ca.smartsprout.it.smart.smarthomegarden.ui.AccountSettingsActivity;
import ca.smartsprout.it.smart.smarthomegarden.ui.adapter.ProfilePlantAdapter;
import ca.smartsprout.it.smart.smarthomegarden.utils.GridSpacingDecoration;
import ca.smartsprout.it.smart.smarthomegarden.utils.ImagePickerHandler;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.PlantViewModel;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.ProfilePlantViewModel;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.UserViewModel;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.PhotoViewModel;
import ca.smartsprout.it.smart.smarthomegarden.ui.adapter.PhotoAdapter;


public class ProfileFragment extends Fragment {

    boolean isOptionsVisible;
    private ImageView imageView;
    private TextView userNameTV, plantsCountTV;
    private ExtendedFloatingActionButton fabAddPlant, fabAddPicture, fabAddTask;
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
    private ActivityResultLauncher<Uri> cameraLauncher;
    private ActivityResultLauncher<String> galleryLauncher;
    private String currentDate;
    private PhotoRepository photoRepository;
    private Uri capturedImageUri;


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
                        openCamera();
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
                        openGallery();
                    } else {
                        Toast.makeText(requireContext(), "Gallery permission denied.", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Initialize camera launcher
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                result -> {
                    if (result && capturedImageUri != null) {
                        Photo photo = new Photo(capturedImageUri.toString(), currentDate);
                        adapter.addPhoto(photo);
                        photoRepository.uploadImageToFirebase(capturedImageUri, userViewModel.getUserId());
                    } else {
                        Toast.makeText(requireContext(), "Failed to capture image.", Toast.LENGTH_SHORT).show();
                    }
                }

        );

        // Initialize gallery launcher
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        capturedImageUri = uri;
                        Photo photo = new Photo(uri.toString(), currentDate);
                        adapter.addPhoto(photo);
                        photoRepository.uploadImageToFirebase(uri, userViewModel.getUserId());
                    } else {
                        Toast.makeText(requireContext(), "Failed to select image.", Toast.LENGTH_SHORT).show();
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
        final ExtendedFloatingActionButton fabAddPlant = view.findViewById(R.id.addplant);
        final ExtendedFloatingActionButton fabAddPicture = view.findViewById(R.id.camera);
        final ExtendedFloatingActionButton fabAddTask = view.findViewById(R.id.addtask);

        // All the FABs are initially hidden
        fabAddPlant.setVisibility(View.GONE);
        fabAddPicture.setVisibility(View.GONE);
        fabAddTask.setVisibility(View.GONE);

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


        fabMain = view.findViewById(R.id.floatingActionButton);

        // Toggle FAB menu with translationY animation based on your example
        fabMain.setOnClickListener(v -> {
            // Toggle the visibility of the FAB menu
            isOptionsVisible = !isOptionsVisible;
            if (isOptionsVisible) {
                fabAddPlant.setVisibility(View.VISIBLE);
                fabAddPicture.setVisibility(View.VISIBLE);
                fabAddTask.setVisibility(View.VISIBLE);

                // Animate up
                fabAddPlant.extend();
                fabAddPicture.extend();
                fabAddTask.extend();

            } else {
                fabAddPlant.shrink();
                fabAddPicture.shrink();
                fabAddTask.shrink();

                // Animate them down and hide after
                fabAddPlant.animate().translationY(0)
                        .withEndAction(() -> fabAddPlant.setVisibility(View.GONE))
                        .start();

                fabAddPicture.animate().translationY(0)
                        .withEndAction(() -> fabAddPicture.setVisibility(View.GONE))
                        .start();

                fabAddTask.animate().translationY(0)
                        .withEndAction(() -> fabAddTask.setVisibility(View.GONE))
                        .start();

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

        fabAddPicture.setOnClickListener(v -> {

            new com.google.android.material.dialog.MaterialAlertDialogBuilder( requireContext())
                    .setTitle("Add a Photo")
                    .setMessage("Choose how you'd like to upload a photo to track your plant's growth.")
                    .setIcon(R.drawable.ic_add_img)
                    .setPositiveButton("Camera", (dialog, which) -> openCamera())
                    .setNegativeButton("Gallery", (dialog, which) -> openGallery())
                    .setNeutralButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .show();
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
        plantsCountTV = view.findViewById(R.id.plantsCountTV);


        ProfilePlantViewModel viewModel = new ViewModelProvider(this).get(ProfilePlantViewModel.class);
        PlantViewModel plantViewModel = new ViewModelProvider(this).get(PlantViewModel.class);
        ProfilePlantAdapter adapter = new ProfilePlantAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // Observe plant list
        plantViewModel.getAllPlants().observe(getViewLifecycleOwner(), plants -> {
            adapter.updatePlantList(plants);
            for (Plant p : plants) {
                viewModel.loadSensorDataForPlant(p.getId());
            }
        });

        viewModel.getSensorDataMap().observe(getViewLifecycleOwner(), adapter::updateSensorDataMap);


        photosGrid.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        photosGrid.addItemDecoration(new GridSpacingDecoration(14));


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

        userViewModel.getUserID().observe(getViewLifecycleOwner(), userId -> {
            if (userId != null) {
                Log.d("ProfileFragment", "UserID found: " + userId);
                fetchPlantCount(userId); // Fetch plant count only when userId is available
            } else {
                Log.e("ProfileFragment", "UserID is null!");
            }
        });

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

    private void fetchPlantCount(String userId) {
        PlantRepository plantRepository = new PlantRepository();
        plantRepository.fetchPlantCount(userId, new PlantRepository.PlantCountCallback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(int count) {
                Log.d("ProfileFragment", "Plant count: " + count);

                // ✅ Avoid IllegalStateException
                if (isAdded() && getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        String plantText = count == 1 ? "Plant" : "Plants";
                        plantsCountTV.setText(count + " " + plantText);
                    });
                }
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("ProfileFragment", "Error fetching plant count: " + errorMessage);
            }
        });
    }

    private void openCamera() {
        File photoFile = new File(requireContext().getExternalFilesDir(null), "photo_" + System.currentTimeMillis() + ".jpg");
        capturedImageUri = FileProvider.getUriForFile(requireContext(), requireContext().getPackageName() + ".provider", photoFile);
        cameraLauncher.launch(capturedImageUri);
    }

    private void openGallery() {
        galleryLauncher.launch("image/*");
    }



}