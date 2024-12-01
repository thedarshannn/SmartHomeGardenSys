package ca.smartsprout.it.smart.smarthomegarden.viewmodels;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class PhotoViewModel extends AndroidViewModel {

    private final MutableLiveData<List<String>> photos = new MutableLiveData<>();

    public PhotoViewModel() {
        super();
        loadPhotos(); // Load mock data for now
    }

    public LiveData<List<String>> getPhotos() {
        return photos;
    }

    private void loadPhotos() {
        // Mock data
        List<String> photoList = new ArrayList<>();
        photoList.add("https://example.com/photo1.jpg");
        photoList.add("https://example.com/photo2.jpg");
        photoList.add("https://example.com/photo3.jpg");
        photos.setValue(photoList);

    }
}
