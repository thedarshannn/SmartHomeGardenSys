/**
 * Smart Sprout
 * Members:
 * 1. Aditi Patel, n01525570, CENG322-RCB
 * 2. Birava Prajapati, n01579924, CENG322-RCA
 * 3. Darshankumar Prajapati, n01584247, CENG322-RCB
 * 4. Zeel Patel, n01526282, CENG322-RCB
 */

package ca.smartsprout.it.smart.smarthomegarden.viewmodels;

import static ca.smartsprout.it.smart.smarthomegarden.utils.Constants.KEY_USER_NAME;
import static ca.smartsprout.it.smart.smarthomegarden.utils.Constants.KEY_USER_PIC;
import static ca.smartsprout.it.smart.smarthomegarden.utils.Constants.PREFS_USER_PROFILE;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


public class AccountSettingsViewModel extends AndroidViewModel {

    private final SharedPreferences sharedPreferences;
    private final MutableLiveData<Uri> profileImageUri = new MutableLiveData<>();
    private final MutableLiveData<String> userName = new MutableLiveData<>();

    public AccountSettingsViewModel(Application application) {
        super(application);
        sharedPreferences = application.getSharedPreferences(PREFS_USER_PROFILE, Context.MODE_PRIVATE);
        loadProfileData();

    }
    public LiveData<Uri> getProfileImageUri() {
        return profileImageUri;
    }

    public LiveData<String> getUserName() {
        return userName;
    }

    public void loadProfileData() {
        String uriString = sharedPreferences.getString(KEY_USER_PIC, null);
        if (uriString != null) {
            try{
                profileImageUri.setValue(Uri.parse(uriString));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void saveProfileImageUri(Uri uri) {
        if (uri != null) {
            try {
                profileImageUri.setValue(uri);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(KEY_USER_PIC, uri.toString());
                editor.apply();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
