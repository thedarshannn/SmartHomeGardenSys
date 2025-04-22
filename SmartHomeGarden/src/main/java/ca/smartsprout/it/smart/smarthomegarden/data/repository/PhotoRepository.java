/**
 * Smart Sprout
 * Members:
 * 1. Aditi Patel, n01525570, CENG322-RCB
 * 2. Birava Prajapati, n01579924, CENG322-RCA
 * 3. Darshankumar Prajapati, n01574247, CENG322-RCB
 * 4. Zeel Patel, n01526282, CENG322-RCB
 */
package ca.smartsprout.it.smart.smarthomegarden.data.repository;

import static android.content.ContentValues.TAG;
import static androidx.test.InstrumentationRegistry.getContext;

import android.app.Application;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ca.smartsprout.it.smart.smarthomegarden.data.PhotoDatabase;
import ca.smartsprout.it.smart.smarthomegarden.data.dao.PhotoDao;
import ca.smartsprout.it.smart.smarthomegarden.data.model.Photo;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PhotoRepository {

    private final FirebaseStorage storage;
    private final StorageReference storageRef;
    private final MutableLiveData<List<Photo>> photosLiveData;
    private final PhotoDao photoDao;
    private final ExecutorService executorService;

    public PhotoRepository(Application application) {
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference().child("plants_picture/");
        photosLiveData = new MutableLiveData<>(new ArrayList<>());
        photoDao = PhotoDatabase.getInstance(application).photoDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Photo>> getAllPhotos() {
        return photosLiveData;
    }

    public void uploadImageToFirebase(Uri imageUri, String userId) {
        if (imageUri != null) {
            StorageReference imageRef = storageRef.child(userId + "/" + UUID.randomUUID().toString());

            imageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                        Photo photo = new Photo(uri.toString(), currentDate);
                        List<Photo> currentPhotos = photosLiveData.getValue();
                        currentPhotos.add(0, photo);
                        photosLiveData.setValue(currentPhotos);
                        markPhotoAsSynced(photo); // Mark the photo as synced
                    }))
                    .addOnFailureListener(e -> {
                        // Handle unsuccessful uploads
                        Toast.makeText(getContext(), "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    public void markPhotoAsSynced(Photo photo) {
        photo.setSynced(true); // Mark the photo as synced
        executorService.execute(() -> photoDao.updatePhoto(photo)); // Update in Room database
    }


    public void fetchPhotosFromFirebase(String userId) {
        storageRef.child(userId).listAll().addOnSuccessListener(listResult -> {
            for (StorageReference fileRef : listResult.getItems()) {
                fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                    Photo photo = new Photo(uri.toString(), date);
                    insertPhoto(photo); // Save to local DB
                    List<Photo> currentPhotos = photosLiveData.getValue();
                    currentPhotos.add(photo);
                    photosLiveData.postValue(currentPhotos);
                });
            }
        }).addOnFailureListener(e -> {
            Log.e(TAG, "Failed to fetch photos: " + e.getMessage());
        });
    }


    public void insertPhoto(Photo photo) {
        executorService.execute(() -> photoDao.insertPhoto(photo));
    }


    public void deletePhotoFromFirestore(Photo photo) {
        String url = photo.getUrl();

        if (url != null && url.startsWith("https://firebasestorage.googleapis.com")) {
            try {
                StorageReference photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(url);
                photoRef.delete().addOnSuccessListener(aVoid -> {
                    // Photo deleted successfully from Firebase Storage
                    executorService.execute(() -> photoDao.deletePhoto(photo));
                }).addOnFailureListener(exception -> {
                    Log.e("PhotoRepository", "Failed to delete from Firebase Storage: " + exception.getMessage());
                });
            } catch (IllegalArgumentException e) {
                Log.e("PhotoRepository", "Invalid Firebase Storage URL: " + url);
            }
        } else {
            Log.w("PhotoRepository", "Skipping deletion: Not a Firebase Storage URL: " + url);
            executorService.execute(() -> photoDao.deletePhoto(photo)); // Still delete from Room
        }
    }


}
