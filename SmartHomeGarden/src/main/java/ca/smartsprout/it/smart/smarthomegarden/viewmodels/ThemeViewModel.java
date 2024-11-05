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
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ca.smartsprout.it.smart.smarthomegarden.R;

public class ThemeViewModel extends AndroidViewModel {

    private static final String THEME_KEY = "app_theme";
    private final MutableLiveData<String> themeMode = new MutableLiveData<>();
    private final SharedPreferences sharedPreferences;

    public ThemeViewModel(@NonNull Application application) {
        super(application);
        sharedPreferences = application.getSharedPreferences(application.getString(R.string.mysharedpref), Context.MODE_PRIVATE);
        themeMode.setValue(sharedPreferences.getString(THEME_KEY, "system"));
    }

    public LiveData<String> getThemeMode() {
        return themeMode;
    }

    public void setThemeMode(String mode) {
        themeMode.setValue(mode);
        sharedPreferences.edit().putString(THEME_KEY, mode).apply();
        switch (mode) {
            case "light":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case "dark":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case "system":
            default:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }
    }
}