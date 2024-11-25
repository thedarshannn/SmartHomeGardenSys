/**
 * Smart Sprout
 * Members:
 * 1. Aditi Patel, n01525570, CENG322-RCB
 * 2. Birava Prajapati, n01579924, CENG322-RCA
 * 3. Darshankumar Prajapati, n01584247, CENG322-RCB
 * 4. Zeel Patel, n01526282, CENG322-RCB
 */

package ca.smartsprout.it.smart.smarthomegarden.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.AccountSettingsViewModel;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.PasswordViewModel;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.UserViewModel;

public class AccountSettingsActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 100;

    private AccountSettingsViewModel viewModel;
    private ImageView profileImageView;
    private EditText editUserName,emailEditText,editcurrentPasswordText,editPasswordText,retypePasswordText;
    private TextInputLayout currentPasswordLayout, newPasswordLayout, retypePasswordLayout;
    private Uri cameraImageUri; // To store the camera image URI temporarily
    private UserViewModel userViewModel;
    private PasswordViewModel passwordViewModel;
    private final Handler handler = new Handler();
    private Runnable runnable;



    private final ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    profileImageView.setImageURI(cameraImageUri); // Update ImageView

                    // Use ImageDecoder to get Bitmap
                    try {
                        Bitmap bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(this.getContentResolver(), cameraImageUri));
                        viewModel.uploadProfilePicture(bitmap, userViewModel.getUserId()); // Upload image
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Failed to load image.", Toast.LENGTH_SHORT).show();
                    }
                }
            });



    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    assert result.getData() != null;
                    Uri selectedImageUri = result.getData().getData();
                    profileImageView.setImageURI(selectedImageUri); // Update ImageView

                    // Use ImageDecoder to get Bitmap
                    try {
                        Bitmap bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(this.getContentResolver(), selectedImageUri));
                        viewModel.uploadProfilePicture(bitmap, userViewModel.getUserId()); // Upload image
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Failed to load image.", Toast.LENGTH_SHORT).show();
                    }
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        profileImageView = findViewById(R.id.editProfilePic);
        editUserName = findViewById(R.id.editUserName);
        emailEditText = findViewById(R.id.etCurrentEmail);
        editPasswordText = findViewById(R.id.etNewPassword);
        editcurrentPasswordText = findViewById(R.id.etCurrentPassword);
        retypePasswordText = findViewById(R.id.etRetypePassword);
        currentPasswordLayout = findViewById(R.id.CurrentPasswordLayout);
        newPasswordLayout = findViewById(R.id.NewPasswordLayout);
        retypePasswordLayout = findViewById(R.id.retypePasswordLayout);
        Button saveButton = findViewById(R.id.saveButton);
        Button updatePasswordButton = findViewById(R.id.btnUpdatePassword);

        // Initialize ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Handle the back button press
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish(); // Close the activity and go back
            }
        });

        viewModel = new ViewModelProvider(this).get(AccountSettingsViewModel.class);
        passwordViewModel = new ViewModelProvider(this).get(PasswordViewModel.class);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        userViewModel.getProfilePictureUrl().observe(this, profilePictureUrl -> {
            if (profilePictureUrl != null) {
                Glide.with(this)
                        .load(profilePictureUrl)
                        .placeholder(R.drawable.user)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(profileImageView);
            }
        });
        userViewModel.getUserName().observe(this, name -> editUserName.setText(name));
        // Observe email LiveData
        userViewModel.getUserEmail().observe(this, email -> {
            if (email != null) {
                emailEditText.setText(email);
            } else {
                emailEditText.setText(R.string.email_not_found);
            }
        });

        // Click listener for profile image edit
        profileImageView.setOnClickListener(v -> showImagePickerDialog());

        // Save button click listener
        saveButton.setOnClickListener(v -> {
            String newName = editUserName.getText().toString();
            Uri selectedImageUri = viewModel.getProfileImageUri().getValue();

            if (newName.isEmpty()) {
                editUserName.setError(getString(R.string.name_cannot_empty));
                Toast.makeText(this, R.string.valid_full_name, Toast.LENGTH_SHORT).show();
                return; // Stop further execution
            }

            if (selectedImageUri != null) {
                try {
                    Bitmap bitmap;

                    bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), selectedImageUri));

                    // Upload the profile picture
                    viewModel.uploadProfilePicture(bitmap, userViewModel.getUserId());

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed to load image.", Toast.LENGTH_SHORT).show();
                }
            }



            // Save the valid full name
            userViewModel.updateUserName(newName);

            Toast.makeText(this, R.string.profile_saved, Toast.LENGTH_SHORT).show();
        });

          // Validate current password
        editcurrentPasswordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                handler.removeCallbacks(runnable); // Remove previously queued tasks
                runnable = () -> {
                    String currentPassword = s.toString().trim();
                    if (!currentPassword.isEmpty()) {
                        passwordViewModel.validateCurrentPassword(currentPassword); // Validate with Firebase
                    } else {
                        currentPasswordLayout.setError(getString(R.string.password_cannot_be_empty));
                        passwordViewModel.handleEmptyPassword();
                    }
                };
                handler.postDelayed(runnable, 500); // Delay validation by 500ms
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        // Observe current password validation and update UI
        passwordViewModel.getCurrentPasswordValidation().observe(this, isValid -> {
            if (isValid != null) {
                if (isValid) {
                    currentPasswordLayout.setHelperText(getString(R.string.password_is_correct));
                    currentPasswordLayout.setError(null); // Clear error
                } else {
                    currentPasswordLayout.setHelperText(null);
                    currentPasswordLayout.setError(getString(R.string.incorrect_password));
                }
            }
        });

        // Observe password matching and update UI
        TextWatcher passwordMatchWatcher = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String newPassword = editPasswordText.getText().toString();
                String retypePassword = retypePasswordText.getText().toString();
                passwordViewModel.checkPasswordsMatch(newPassword, retypePassword);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        };

        editPasswordText.addTextChangedListener(passwordMatchWatcher);
        retypePasswordText.addTextChangedListener(passwordMatchWatcher);

        passwordViewModel.getPasswordsMatch().observe(this, doPasswordsMatch -> {
            if (doPasswordsMatch != null) {
                if (doPasswordsMatch) {
                    retypePasswordLayout.setHelperText(getString(R.string.passwords_match));
                    retypePasswordLayout.setError(null); // Clear error
                } else {
                    retypePasswordLayout.setHelperText(null);
                    retypePasswordLayout.setError(getString(R.string.passwords_do_not_match));
                }
            }
        });

        // Add TextWatcher for "New Password" field
        editPasswordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Clear error dynamically
                newPasswordLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Button click listener for password update
        updatePasswordButton.setOnClickListener(v -> {
            String currentPassword = editcurrentPasswordText.getText().toString().trim();
            String newPassword = editPasswordText.getText().toString().trim();
            // Regex for password validation
            String passwordPattern = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$";

            if (currentPassword.isEmpty()) {
                currentPasswordLayout.setError(getString(R.string.current_password_cannot_be_empty));
                return;
            }

            if (newPassword.isEmpty()) {
                newPasswordLayout.setError(getString(R.string.new_password_cannot_be_empty));
                return;
            }

            if (newPassword.equals(currentPassword)) {
                newPasswordLayout.setError(getString(R.string.cannot_be_same_as_current_password));
                return;
            }

            if (!newPassword.matches(passwordPattern)) {
                newPasswordLayout.setError(getString(R.string.regex));
                return;
            }

            passwordViewModel.updatePassword(currentPassword, newPassword);
        });

        // Observe update status
        passwordViewModel.getUpdateStatus().observe(this, status -> {
            if (status != null) {
                if (status) {
                    Toast.makeText(this, R.string.password_updated, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, R.string.failed_to_update, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Close the activity and go back
        return true;
    }
    private void showImagePickerDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.choose_option)
                .setMessage(R.string.select_camera_or_gallery)
                .setPositiveButton(R.string.camera, (dialog, which) -> {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
                    } else {
                        openCamera();
                    }
                })
                .setNegativeButton(R.string.gallery, (dialog, which) -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13 and above
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, PERMISSION_REQUEST_CODE);
                        } else {
                            openGallery();
                        }
                    } else { // For Android 12 and below
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                        } else {
                            openGallery();
                        }
                    }
                }).show();
    }

    private void openCamera() {
        try {
            File photoFile = createImageFile();
            cameraImageUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".fileprovider", photoFile);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri);
            cameraLauncher.launch(intent);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, R.string.failed_to_create_image, Toast.LENGTH_SHORT).show();
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(intent);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(null);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (permissions[0].equals(Manifest.permission.CAMERA)) {
                    openCamera();
                } else if (permissions[0].equals(Manifest.permission.READ_EXTERNAL_STORAGE) ||
                        permissions[0].equals(Manifest.permission.READ_MEDIA_IMAGES)) {
                    openGallery();
                }else {
                    Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}