/**
 * Smart Sprout
 * Members:
 * 1. Aditi Patel, n01525570, CENG322-RCB
 * 2. Birava Prajapati, n01579924, CENG322-RCA
 * 3. Darshankumar Prajapati, n01584247, CENG322-RCB
 * 4. Zeel Patel, n01526282, CENG322-RCB
 */

package ca.smartsprout.it.smart.smarthomegarden.ui;

import static ca.smartsprout.it.smart.smarthomegarden.utils.ImagePickerHandler.openCamera;
import static ca.smartsprout.it.smart.smarthomegarden.utils.ImagePickerHandler.openGallery;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;

import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.utils.ImagePickerHandler;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.AccountSettingsViewModel;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.PasswordViewModel;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.UserViewModel;

public class AccountSettingsActivity extends AppCompatActivity {

    private AccountSettingsViewModel viewModel;
    private ImageView profileImageView;
    private EditText editUserName,emailEditText,editcurrentPasswordText,editPasswordText,retypePasswordText;
    private TextInputLayout currentPasswordLayout, newPasswordLayout, retypePasswordLayout;
    private Uri cameraImageUri; // To store the camera image URI temporarily
    private UserViewModel userViewModel;
    private PasswordViewModel passwordViewModel;
    private final Handler handler = new Handler();
    private Runnable runnable;
    private TextView tvForgotPassword;


    private final ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Uri cameraImageUri = viewModel.getProfileImageUri().getValue();
                    if (cameraImageUri != null) {
                        try {
                            Bitmap bitmap = ImageDecoder.decodeBitmap(
                                    ImageDecoder.createSource(this.getContentResolver(), cameraImageUri)
                            );
                            viewModel.uploadProfilePicture(bitmap, userViewModel.getUserId());
                            profileImageView.setImageURI(cameraImageUri);
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(this, "Error decoding image.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "No image captured. URI is null.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Image capture cancelled.", Toast.LENGTH_SHORT).show();
                }
            }
    );


    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri selectedImageUri = result.getData().getData();
                    if (selectedImageUri != null) {
                        try {
                            Bitmap bitmap = ImageDecoder.decodeBitmap(
                                    ImageDecoder.createSource(this.getContentResolver(), selectedImageUri)
                            );
                            viewModel.uploadProfilePicture(bitmap, userViewModel.getUserId());
                            profileImageView.setImageURI(selectedImageUri);
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(this, "Error decoding image.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "No image selected. URI is null.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Image selection cancelled.", Toast.LENGTH_SHORT).show();
                }
            }
    );



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);

// Enable back navigation
        toolbar.setNavigationOnClickListener(v -> finish());


        profileImageView = findViewById(R.id.editProfilePic);
        editUserName = findViewById(R.id.editUserName);
        emailEditText = findViewById(R.id.etCurrentEmail);
        editPasswordText = findViewById(R.id.etNewPassword);
        editcurrentPasswordText = findViewById(R.id.etCurrentPassword);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        retypePasswordText = findViewById(R.id.etRetypePassword);
        currentPasswordLayout = findViewById(R.id.CurrentPasswordLayout);
        newPasswordLayout = findViewById(R.id.NewPasswordLayout);
        retypePasswordLayout = findViewById(R.id.retypePasswordLayout);
        Button saveButton = findViewById(R.id.saveButton);
        Button updatePasswordButton = findViewById(R.id.btnUpdatePassword);

        tvForgotPassword.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(this, "Email not available for reset.", Toast.LENGTH_SHORT).show();
                return;
            }

            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Password reset link sent to " + email, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(this, "Failed to send reset email.", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

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
        profileImageView.setOnClickListener(v -> {
            ImagePickerHandler.showImagePickerDialog(this, cameraLauncher, galleryLauncher,null,null);
        });

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

        // Observe password matching
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
            // Check for empty current password
            if (currentPassword.isEmpty()) {
                Toast.makeText(this, R.string.current_password_cannot_be_empty, Toast.LENGTH_SHORT).show();
                return;
            }

            // Check for empty new password
            if (newPassword.isEmpty()) {
                Toast.makeText(this, R.string.new_password_cannot_be_empty, Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if current and new passwords are the same
            if (newPassword.equals(currentPassword)) {
                Toast.makeText(this, R.string.cannot_be_same_as_current_password, Toast.LENGTH_SHORT).show();
                return;
            }

            // Validate regex for new password
            if (!newPassword.matches(passwordViewModel.getPasswordPattern())) {
                newPasswordLayout.setError(getString(R.string.regex));
                 return;
            }
            passwordViewModel.updatePassword(currentPassword, newPassword);
        });
        // Observe Regex Validation for New Password
        passwordViewModel.getPasswordRegexValidation().observe(this, isValid -> {
            if (isValid != null) {
                if (!isValid) {
                    newPasswordLayout.setError(getString(R.string.regex));
                } else {
                    newPasswordLayout.setError(null);
                }
            }
        });

    }
    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Close the activity and go back
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) { // Camera permission
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Open the camera immediately
                openCamera(this, cameraLauncher);
            } else {
                Toast.makeText(this, "Camera permission denied.", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == 101) { // Gallery permission
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Open the gallery immediately
                openGallery(this, galleryLauncher);
            } else {
                Toast.makeText(this, "Gallery permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}