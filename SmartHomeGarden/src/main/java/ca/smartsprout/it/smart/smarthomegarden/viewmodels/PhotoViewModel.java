package ca.smartsprout.it.smart.smarthomegarden.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
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
    public void insertPhoto(Photo photo) {
        photoRepository.insertPhoto(photo);
    }

    public LiveData<List<Photo>> getAllPhotos() {
        return photos;
    }

    public void syncPhotos(String userId) {
        photoRepository.syncPhotos(userId);
    }

}
