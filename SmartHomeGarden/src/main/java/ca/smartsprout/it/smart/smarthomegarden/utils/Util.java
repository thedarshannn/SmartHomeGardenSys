/**
 * Smart Sprout
 * Members:
 * 1. Aditi Patel, n01525570, CENG322-RCB
 * 2. Birava Prajapati, n01579924, CENG322-RCA
 * 3. Darshankumar Prajapati, n01584247, CENG322-RCB
 * 4. Zeel Patel, n01526282, CENG322-RCB
 */
package ca.smartsprout.it.smart.smarthomegarden.utils;

import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.view.View;
import com.google.android.material.snackbar.Snackbar;

public class Util {

    public static void showSnackbar(View view, String message, boolean action) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        if (action) {
            snackbar.setAction("Settings", v -> {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + view.getContext().getPackageName()));
                view.getContext().startActivity(intent);
            });
        }
        snackbar.show();
    }

    public static String getUVLevelDescription(float uv) {
        if (uv <= 2) return "Low";
        else if (uv <= 5) return "Moderate";
        else if (uv <= 7) return "High";
        else if (uv <= 10) return "Very High";
        else return "Extreme";
    }

    public static int convertMoistureToPercentage(float rawMoisture) {
        return (int) ((rawMoisture / 1023f) * 100);
    }
}
