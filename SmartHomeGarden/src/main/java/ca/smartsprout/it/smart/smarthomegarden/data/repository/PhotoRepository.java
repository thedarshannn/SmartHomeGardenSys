/**
 * Smart Sprout
 * Members:
 * 1. Aditi Patel, n01525570, CENG322-RCB
 * 2. Birava Prajapati, n01579924, CENG322-RCA
 * 3. Darshankumar Prajapati, n01574247, CENG322-RCB
 * 4. Zeel Patel, n01526282, CENG322-RCB
 */
package ca.smartsprout.it.smart.smarthomegarden.data.repository;

import static androidx.test.InstrumentationRegistry.getContext;

import android.app.Application;
import android.net.Uri;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ca.smartsprout.it.smart.smarthomegarden.data.model.Photo;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PhotoRepository {

    private final FirebaseStorage storage;
    private final StorageReference storageRef;
    private final MutableLiveData<List<Photo>> photosLiveData;

    public PhotoRepository(Application application) {
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference().child("plants_picture/");
        photosLiveData = new MutableLiveData<>(new ArrayList<>());
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
                    }))
                    .addOnFailureListener(e -> {
                        // Handle unsuccessful uploads
                        Toast.makeText(getContext(), "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }
}