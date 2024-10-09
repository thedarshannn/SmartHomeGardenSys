package ca.smartsprout.it.smart.smarthomegarden.ui;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Apply the screen orientation when the activity starts
        applyScreenOrientation();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Apply the screen orientation when the activity resumes
        applyScreenOrientation();
    }

    void applyScreenOrientation() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String orientation = sharedPreferences.getString("screen_orientation", "auto");

        switch (orientation) {
            case "portrait":
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;
            case "landscape":
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
            default:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                break;
        }
    }
}
