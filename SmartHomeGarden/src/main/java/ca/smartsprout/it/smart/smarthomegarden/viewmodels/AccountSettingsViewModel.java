/**
 * Smart Sprout
 * Members:
 * 1. Aditi Patel, n01525570, CENG322-RCB
 * 2. Birava Prajapati, n01579924, CENG322-RCA
 * 3. Darshankumar Prajapati, n01584247, CENG322-RCB
 * 4. Zeel Patel, n01526282, CENG322-RCB
 */

package ca.smartsprout.it.smart.smarthomegarden.viewmodels;


import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.test.core.app.ApplicationProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import ca.smartsprout.it.smart.smarthomegarden.data.repository.FirebaseRepository;


public class AccountSettingsViewModel extends AndroidViewModel {

    private final MutableLiveData<Bitmap> profilePictureLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> userName = new MutableLiveData<>();
    private final FirebaseRepository firebaseRepository = new FirebaseRepository();
    private MutableLiveData<Uri> profileImageUri;

    public AccountSettingsViewModel(Application application) {
        super(application);
        // Fetch user details from Firebase
        firebaseRepository.fetchUserDetails().observeForever(user -> {
            if (user != null) {
                userName.setValue(user.getName());
            }
        });
    }

    public LiveData<Bitmap> getProfilePictureLiveData() {
        return profilePictureLiveData;
    }

    public LiveData<String> getUserName() {
        return userName;
    }

    public LiveData<Uri> getProfileImageUri() {
        if (profileImageUri == null) {
            profileImageUri = new MutableLiveData<>();
        }
        return profileImageUri;
    }


    public void saveProfileImageUri(Uri uri) {
        profileImageUri.setValue(uri);
    }

    public void uploadProfilePicture(Bitmap bitmap, String userId) {
        if (bitmap == null || userId == null || userId.isEmpty()) {
            return;
        }

        firebaseRepository.uploadProfilePicture(bitmap, userId, task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                String downloadUrl = task.getResult().toString();
                firebaseRepository.saveProfilePictureUrl(userId, downloadUrl);
                fetchProfilePicture(userId); // Refresh after upload
            }
        });
    }


    public void fetchProfilePicture(String userId) {
        if (userId == null || userId.isEmpty()) {
            return; // Avoid unnecessary calls
        }

        firebaseRepository.getProfilePictureUrl(userId).observeForever(url -> {
            if (url != null && !url.isEmpty()) {
                Glide.with(getApplication().getApplicationContext())
                        .asBitmap()
                        .load(url)
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                profilePictureLiveData.setValue(resource);
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {
                                // Optional: handle if needed
                            }
                        });
            } else {
                // Set a default placeholder image
                profilePictureLiveData.setValue(null); // Or set a placeholder Bitmap
            }
        });
    }


}
