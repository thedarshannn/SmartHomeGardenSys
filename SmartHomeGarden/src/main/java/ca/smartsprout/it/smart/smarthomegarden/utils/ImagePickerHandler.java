package ca.smartsprout.it.smart.smarthomegarden.utils;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ca.smartsprout.it.smart.smarthomegarden.R;
public class ImagePickerHandler {

    private static final int MAX_PERMISSION_DENIALS = 3;

    public static final String PREF_PERMISSION_DENIAL_COUNT_CAMERA = "permission_denial_count_camera";
    public static final String PREF_PERMISSION_DENIAL_COUNT_GALLERY = "permission_denial_count_gallery";

    public interface ImagePickerCallback {
        void onImagePicked(Uri imageUri);
    }

    public static void showImagePickerDialog(
            AppCompatActivity activity,
            ActivityResultLauncher<Intent> cameraLauncher,
            ActivityResultLauncher<Intent> galleryLauncher
    ) {
        new AlertDialog.Builder(activity)
                .setTitle(R.string.choose_option)
                .setMessage(R.string.select_camera_or_gallery)
                .setPositiveButton(R.string.camera, (dialog, which) -> {
                    handlePermissionRequest(
                            activity,
                            Manifest.permission.CAMERA,
                            PREF_PERMISSION_DENIAL_COUNT_CAMERA,
                            100,
                            R.string.camera_permission_required_message,
                            () -> openCamera(activity, cameraLauncher)
                    );
                })
                .setNegativeButton(R.string.gallery, (dialog, which) -> {
                    String permission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                            ? Manifest.permission.READ_MEDIA_IMAGES
                            : Manifest.permission.READ_EXTERNAL_STORAGE;

                    handlePermissionRequest(
                            activity,
                            permission,
                            PREF_PERMISSION_DENIAL_COUNT_GALLERY,
                            101,
                            R.string.gallery_permission_required_message,
                            () -> openGallery(activity, galleryLauncher)
                    );
                }).show();
    }

    private static void handlePermissionRequest(
            AppCompatActivity activity,
            String permission,
            String preferenceKey,
            int requestCode,
            int messageResId,
            Runnable onPermissionGranted
    ) {
        SharedPreferences preferences = activity.getSharedPreferences("permission_prefs", Context.MODE_PRIVATE);
        int denialCount = preferences.getInt(preferenceKey, 0);

        if (ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED) {
            onPermissionGranted.run();
        } else if (denialCount < MAX_PERMISSION_DENIALS) {
            ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
        } else {
            showPermissionRequiredDialog(activity, messageResId);
        }
    }

    public static void handlePermissionResult(
            AppCompatActivity activity,
            String permission,
            int[] grantResults,
            String preferenceKey
    ) {
        SharedPreferences preferences = activity.getSharedPreferences("permission_prefs", Context.MODE_PRIVATE);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            int denialCount = preferences.getInt(preferenceKey, 0);

            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                // User selected "Don't Ask Again", redirect to settings
                showPermissionRequiredDialog(activity, R.string.permission_required_message);
            } else if (denialCount < MAX_PERMISSION_DENIALS) {
                // Increment the denial count
                preferences.edit().putInt(preferenceKey, denialCount + 1).apply();
            }
        }
    }

    private static void showPermissionRequiredDialog(AppCompatActivity activity, int messageResId) {
        new AlertDialog.Builder(activity)
                .setTitle(R.string.permission_required)
                .setMessage(messageResId)
                .setPositiveButton(R.string.go_to_settings, (dialog, which) -> redirectToSettings(activity))
                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss())
                .show();
    }

    private static void redirectToSettings(AppCompatActivity activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivity(intent);
    }

    public static void openCamera(AppCompatActivity activity, ActivityResultLauncher<Intent> launcher) {
        try {
            File photoFile = createImageFile(activity);
            Uri cameraImageUri = FileProvider.getUriForFile(activity,
                    activity.getApplicationContext().getPackageName() + ".fileprovider",
                    photoFile);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri);
            launcher.launch(intent);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(activity, R.string.failed_to_create_image, Toast.LENGTH_SHORT).show();
        }
    }

    public static void openGallery(AppCompatActivity activity, ActivityResultLauncher<Intent> launcher) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        launcher.launch(intent);
    }

    private static File createImageFile(AppCompatActivity activity) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = activity.getExternalFilesDir(null);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }
}

