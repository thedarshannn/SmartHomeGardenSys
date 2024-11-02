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
}
