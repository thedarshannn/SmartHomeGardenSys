/**
 * Smart Sprout
 * Members:
 * 1. Aditi Patel, n01525570, CENG322-RCB
 * 2. Birava Prajapati, n01579924, CENG322-RCA
 * 3. Darshankumar Prajapati, n01574247, CENG322-RCB
 * 4. Zeel Patel, n01526282, CENG322-RCB
 */
package ca.smartsprout.it.smart.smarthomegarden.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import ca.smartsprout.it.smart.smarthomegarden.data.model.Photo;
import ca.smartsprout.it.smart.smarthomegarden.data.repository.PhotoRepository;

public class PhotoViewModel extends AndroidViewModel {

    private final PhotoRepository photoRepository;
    private final LiveData<List<Photo>> photos;


    public PhotoViewModel(@NonNull Application application) {
        super(application);
        photoRepository = new PhotoRepository(application);
        photos = photoRepository.getAllPhotos();
    }


    public LiveData<List<Photo>> getAllPhotos() {
        return photoRepository.getAllPhotos();
    }

    public void fetchPhotosFromFirebase(String userId) {
        photoRepository.fetchPhotosFromFirebase(userId);
    }

    public void deletePhotoFromFirestore(Photo photo) {
        photoRepository.deletePhotoFromFirestore(photo);
    }

}
