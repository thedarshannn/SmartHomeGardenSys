/**
 * Smart Sprout
 * Members:
 * 1. Aditi Patel, n01525570, CENG322-RCB
 * 2. Birava Prajapati, n01579924, CENG322-RCA
 * 3. Darshankumar Prajapati, n01584247, CENG322-RCB
 * 4. Zeel Patel, n01526282, CENG322-RCB
 */

package ca.smartsprout.it.smart.smarthomegarden.data.repository;

import static ca.smartsprout.it.smart.smarthomegarden.utils.Constants.KEY_USER_PIC;
import static ca.smartsprout.it.smart.smarthomegarden.utils.Constants.PREFS_USER_PROFILE;

import android.content.Context;
import android.content.SharedPreferences;

public class AccountRepository {
    private final SharedPreferences sharedPreferences;

    public AccountRepository(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_USER_PROFILE, Context.MODE_PRIVATE);
    }

      public String getProfilePicUri() {
        return sharedPreferences.getString(KEY_USER_PIC, "");
    }

     public void saveProfilePicUri(String uri) {
        sharedPreferences.edit().putString(KEY_USER_PIC, uri).apply();
    }
}
