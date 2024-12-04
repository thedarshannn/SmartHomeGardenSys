/**
 * Smart Sprout
 * Members:
 * 1. Aditi Patel, n01525570, CENG322-RCB
 * 2. Birava Prajapati, n01579924, CENG322-RCA
 * 3. Darshankumar Prajapati, n01574247, CENG322-RCB
 * 4. Zeel Patel, n01526282, CENG322-RCB
 */

package ca.smartsprout.it.smart.smarthomegarden.data.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import ca.smartsprout.it.smart.smarthomegarden.data.PhotoDatabase;
import ca.smartsprout.it.smart.smarthomegarden.data.dao.PhotoDao;
import ca.smartsprout.it.smart.smarthomegarden.data.model.Photo;

public class PhotoRepository {

    private final PhotoDao photoDao;
    private final LiveData<List<Photo>> allPhotos;
    private final DatabaseReference firebaseRef;

    public PhotoRepository(Application application) {
        PhotoDatabase database = PhotoDatabase.getInstance(application);
        photoDao = database.photoDao();
        allPhotos = photoDao.getAllPhotos();

        // Firebase reference
        firebaseRef = FirebaseDatabase.getInstance().getReference("photos");
    }

    public void insertPhoto(Photo photo) {
        new Thread(() -> photoDao.insertPhoto(photo)).start();
    }

    public LiveData<List<Photo>> getAllPhotos() {
        return allPhotos;
    }

    public void syncPhotos(String userId) {
        new Thread(() -> {
            List<Photo> unsyncedPhotos = photoDao.getAllUnsyncedPhotos();
            for (Photo photo : unsyncedPhotos) {
                firebaseRef.child(userId).push().setValue(photo, (error, ref) -> {
                    if (error == null) {
                        // Mark photo as synced
                        photo.setSynced(true);
                        photoDao.updatePhoto(photo);
                    }
                });
            }
        }).start();
    }
}

