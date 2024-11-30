/**
 * Smart Sprout
 * Members:
 * 1. Aditi Patel, n01525570, CENG322-RCB
 * 2. Birava Prajapati, n01579924, CENG322-RCA
 * 3. Darshankumar Prajapati, n01584247, CENG322-RCB
 * 4. Zeel Patel, n01526282, CENG322-RCB
 */

package ca.smartsprout.it.smart.smarthomegarden.utils;

import android.Manifest;
import android.content.Context;
import android.net.Uri;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
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
import ca.smartsprout.it.smart.smarthomegarden.ui.AccountSettingsActivity;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.AccountSettingsViewModel;

public class ImagePickerHandler {


    public static void showImagePickerDialog(
            AppCompatActivity activity,
            ActivityResultLauncher<Intent> cameraLauncher,
            ActivityResultLauncher<Intent> galleryLauncher
    ) {
        new AlertDialog.Builder(activity)
                .setTitle(R.string.choose_option)
                .setMessage(R.string.select_camera_or_gallery)
                .setPositiveButton(R.string.camera, (dialog, which) -> {
                    if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_GRANTED) {
                        openCamera(activity, cameraLauncher);
                    } else {
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, 100);
                    }
                })
                .setNegativeButton(R.string.gallery, (dialog, which) -> {
                    String permission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                            ? Manifest.permission.READ_MEDIA_IMAGES
                            : Manifest.permission.READ_EXTERNAL_STORAGE;

                    if (ContextCompat.checkSelfPermission(activity, permission)
                            == PackageManager.PERMISSION_GRANTED) {
                        openGallery(activity, galleryLauncher);
                    } else {
                        ActivityCompat.requestPermissions(activity, new String[]{permission}, 101);
                    }
                })
                .show();
    }

    public static void openCamera(AppCompatActivity activity, ActivityResultLauncher<Intent> launcher) {
        try {
            File photoFile = createImageFile(activity);
            Uri cameraImageUri = FileProvider.getUriForFile(activity,
                    activity.getApplicationContext().getPackageName() + ".fileprovider",
                    photoFile);

            if (activity instanceof AccountSettingsActivity){
                AccountSettingsViewModel viewModel = new ViewModelProvider(activity).get(AccountSettingsViewModel.class);
                viewModel.saveProfileImageUri(cameraImageUri);
            }


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri);
            launcher.launch(intent);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(activity, "Failed to create image file.", Toast.LENGTH_SHORT).show();
        }
    }


    public static void openGallery(AppCompatActivity activity, ActivityResultLauncher<Intent> launcher) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        launcher.launch(intent);
    }

    private static File createImageFile(Context context) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }
}
