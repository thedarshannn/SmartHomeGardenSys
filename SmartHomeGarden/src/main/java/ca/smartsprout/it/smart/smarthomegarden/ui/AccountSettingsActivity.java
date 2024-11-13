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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.AccountSettingsViewModel;

public class AccountSettingsActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 100;

    private AccountSettingsViewModel viewModel;
    private ImageView profileImageView;
    private EditText editUserName;
    private Uri cameraImageUri; // To store the camera image URI temporarily


    private final ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    profileImageView.setImageURI(cameraImageUri);
                    viewModel.saveProfileImageUri(cameraImageUri); // Save URI to ViewModel
                }
            });

    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri selectedImage = result.getData().getData();
                    profileImageView.setImageURI(selectedImage);
                    viewModel.saveProfileImageUri(selectedImage); // Save URI to ViewModel
                }
            });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        profileImageView = findViewById(R.id.editProfilePic);
        editUserName = findViewById(R.id.editUserName);
        Button saveButton = findViewById(R.id.saveButton);

        viewModel = new ViewModelProvider(this).get(AccountSettingsViewModel.class);

        // Observe LiveData from ViewModel to set the profile image and username
        viewModel.getProfileImageUri().observe(this, uri -> {
            if (uri != null) {
                profileImageView.setImageURI(uri);
            } else {
                profileImageView.setImageResource(R.drawable.user); // default placeholder image
            }
        });
        viewModel.getUserName().observe(this, name -> {
            if (name != null) {
                editUserName.setText(name);
            }
        });

        // Click listener for profile image edit
        profileImageView.setOnClickListener(v -> showImagePickerDialog());

        // Save button click listener
        saveButton.setOnClickListener(v -> {
           String newName = editUserName.getText().toString();
            viewModel.saveUserName(newName);
            viewModel.saveProfileImageUri(cameraImageUri);
            Toast.makeText(this, R.string.profile_saved, Toast.LENGTH_SHORT).show();
        });
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
