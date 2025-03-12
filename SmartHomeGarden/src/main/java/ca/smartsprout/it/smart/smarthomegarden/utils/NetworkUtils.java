package ca.smartsprout.it.smart.smarthomegarden.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ca.smartsprout.it.smart.smarthomegarden.R;

public class NetworkUtils {

    /**
     * Show an offline overlay image on the current screen.
     *
     * @param context  The context of the application.
     * @param rootView The root view of the current screen (e.g., activity or fragment layout).
     */
    public static void showOfflineOverlay(Context context, ViewGroup rootView) {
        // Inflate the offline overlay layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View offlineOverlayView = inflater.inflate(R.layout.offline_overlay, rootView, false);

        // Add the overlay to the root view
        rootView.addView(offlineOverlayView);

        // Set up a retry button (optional)
        offlineOverlayView.findViewById(R.id.button_retry).setOnClickListener(v -> {
            if (isInternetAvailable(context)) {
                // Remove the overlay if the internet is restored
                rootView.removeView(offlineOverlayView);
            } else {
                Toast.makeText(context, R.string.still_no_internet_connection, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Remove the offline overlay image from the current screen.
     *
     * @param rootView The root view of the current screen.
     */
    public static void removeOfflineOverlay(ViewGroup rootView) {
        View offlineOverlayView = rootView.findViewById(R.id.offline_overlay);
        if (offlineOverlayView != null) {
            rootView.removeView(offlineOverlayView);
        }
    }

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
}