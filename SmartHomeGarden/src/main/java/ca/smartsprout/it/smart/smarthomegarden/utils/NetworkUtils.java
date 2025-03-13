package ca.smartsprout.it.smart.smarthomegarden.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.NetworkViewModel;

public class NetworkUtils {

    /**
     * Show an offline overlay image on the current screen.
     *
     * @param context  The context of the application.
     * @param rootView The root view of the current screen (e.g., activity or fragment layout).
     * @param lifecycleOwner The lifecycle owner for observing ViewModel changes.
     */
    public static void showOfflineOverlay(Context context, ViewGroup rootView, LifecycleOwner lifecycleOwner) {
        // Inflate the offline overlay layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View offlineOverlayView = inflater.inflate(R.layout.offline_overlay, rootView, false);

        // Add the overlay to the root view
        rootView.addView(offlineOverlayView);

        // Initialize ViewModel
        if (context instanceof ViewModelStoreOwner) {
            NetworkViewModel networkViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(NetworkViewModel.class);

            // Observe connectivity status
            networkViewModel.getIsConnected().observe(lifecycleOwner, isConnected -> {
                if (isConnected) {
                    // Remove the overlay if the internet is restored
                    rootView.removeView(offlineOverlayView);
                }
            });
        } else {
            throw new IllegalArgumentException("Context must be an instance of ViewModelStoreOwner");
        }

        // Set up retry button
        Button retryButton = offlineOverlayView.findViewById(R.id.buttonRetry);
        retryButton.setOnClickListener(v -> {
            if (isInternetAvailable(context)) {
                rootView.removeView(offlineOverlayView);
            } else {
                Toast.makeText(context, R.string.still_no_internet_connection, Toast.LENGTH_SHORT).show();
            }
        });

        // Set up settings button
        Button settingsButton = offlineOverlayView.findViewById(R.id.buttonSettings);
        settingsButton.setOnClickListener(v -> context.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS)));
    }

    /**
     * Show a dialog indicating no internet connection.
     *
     * @param context The context of the application.
     */
    public static void showNoInternetDialog(Context context) {
        new AlertDialog.Builder(context)
                .setTitle("Oops!!")
                .setMessage("Check Your Internet Connection")
                .setPositiveButton("Retry", (dialog, which) -> {
                    if (!isInternetAvailable(context)) {
                        showNoInternetDialog(context);
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .setIcon(R.drawable.no_wifi)
                .show();
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
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            Network network = connectivityManager.getActiveNetwork();
            if (network != null) {
                NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
                if (networkCapabilities == null) return false;
                return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET);
            }
        }
        return false;
    }
}
