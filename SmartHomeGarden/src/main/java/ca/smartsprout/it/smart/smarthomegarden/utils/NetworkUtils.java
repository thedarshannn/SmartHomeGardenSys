/**
 * Smart Sprout
 * Members:
 * 1. Aditi Patel, n01525570, CENG322-RCB
 * 2. Birava Prajapati, n01579924, CENG322-RCA
 * 3. Darshankumar Prajapati, n01584247, CENG322-RCB
 * 4. Zeel Patel, n01526282, CENG322-RCB
 */

package ca.smartsprout.it.smart.smarthomegarden.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ca.smartsprout.it.smart.smarthomegarden.R;


public class NetworkUtils {

    /**
     * Check if the device is connected to the internet.
     *
     * @param context The context of the application.
     * @return True if the device is connected to the internet, false otherwise.
     */
    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            Network network = connectivityManager.getActiveNetwork();
            if (network != null) {
                NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
                return networkCapabilities != null &&
                        (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
            }
        }
        return false;
    }

    /**
     * Show a dialog to inform the user that there is no internet connection.
     *
     * @param context The context of the application.
     */
    public static void showNoInternetDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Inflate custom layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_no_internet, null);
        builder.setView(dialogView);

        // Find views in custom layout
        ImageView icon = dialogView.findViewById(R.id.dialog_icon);
        TextView title = dialogView.findViewById(R.id.dialog_title);
        TextView message = dialogView.findViewById(R.id.dialog_message);

        // Set dialog buttons
        dialogView.findViewById(R.id.button_retry).setOnClickListener(v -> {
            if (isInternetAvailable(context)) {
                Toast.makeText(context, R.string.internet_connection_restored, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, R.string.still_no_internet_connection, Toast.LENGTH_SHORT).show();
            }
        });

        dialogView.findViewById(R.id.button_settings).setOnClickListener(v -> {
            context.startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
        });

        // Create and show dialog
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }

}
