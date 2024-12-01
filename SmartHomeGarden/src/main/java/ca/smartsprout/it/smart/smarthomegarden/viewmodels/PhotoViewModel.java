package ca.smartsprout.it.smart.smarthomegarden.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import ca.smartsprout.it.smart.smarthomegarden.data.model.Photo;

public class PhotoViewModel extends AndroidViewModel {

    private final MutableLiveData<List<Photo>> photos = new MutableLiveData<>();

    public PhotoViewModel(Application application) {
        super(application);
        loadPhotos(); // Load mock data for now
    }

    public LiveData<List<Photo>> getPhotos() {
        return photos;
    }

    private void loadPhotos() {
        List<Photo> photoList = new ArrayList<>();
        photoList.add(new Photo("1", "https://via.placeholder.com/300x200", "Rose", "2024-12-01"));
        photoList.add(new Photo("2", "https://via.placeholder.com/200x300", "Sunflower", "2024-12-02"));
        photoList.add(new Photo("3", "https://via.placeholder.com/300x300", "Tulip", "2024-12-03"));
        photoList.add(new Photo("4", "https://via.placeholder.com/400x200", "Lily", "2024-12-04"));
        photoList.add(new Photo("5", "https://via.placeholder.com/200x400", "Daisy", "2024-12-05"));
        photos.setValue(photoList);
    }
}
