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
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.lifecycle.AndroidViewModel;

import ca.smartsprout.it.smart.smarthomegarden.ui.LoginActivity;
import ca.smartsprout.it.smart.smarthomegarden.utils.Constants;

public class SessionViewModel extends AndroidViewModel {

    private final String PREFS_USER_SESSION  = Constants.PREFS_USER_SESSION;



    public SessionViewModel(Application application) {
        super(application);
    }

    public void logOut() {
        // Clear session data
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(Constants.PREFS_USER_PROFILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        // Redirect to login screen
        Intent intent = new Intent(getApplication(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        getApplication().startActivity(intent);
    }
}