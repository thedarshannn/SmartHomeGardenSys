package ca.smartsprout.it.smart.smarthomegarden.viewmodels;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

/**
 * ViewModel class to observe the network status.
 * This class is responsible for monitoring the network status and notifying the UI
 * when the network becomes available or unavailable.
 */
public class NetworkViewModel extends AndroidViewModel {

    private final MutableLiveData<Boolean> isConnected = new MutableLiveData<>();
    private ConnectivityManager.NetworkCallback networkCallback;

    public NetworkViewModel(@NonNull Application application) {
        super(application);
        observeNetwork(application.getApplicationContext());
    }

    /**
     * Get the network status.
     *
     * @return LiveData object that represents the network status.
     */
    public LiveData<Boolean> getIsConnected() {
        return isConnected;
    }

    /**
     * Observe the network status.
     *
     * @param context The context of the application.
     */
    private void observeNetwork(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return; // Exit if ConnectivityManager is not available
        }

        // Create a NetworkRequest to monitor network capabilities
        NetworkRequest networkRequest = new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build();

        // Initialize the NetworkCallback
        networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@NonNull Network network) {
                super.onAvailable(network);
                isConnected.postValue(true); // Network is available
            }

            @Override
            public void onLost(@NonNull Network network) {
                super.onLost(network);
                isConnected.postValue(false); // Network is lost
            }

            @Override
            public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
                super.onCapabilitiesChanged(network, networkCapabilities);
                // Update the network status based on the capabilities
                boolean hasInternet = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
                isConnected.postValue(hasInternet);
            }
        };

        // Register the NetworkCallback to monitor network changes
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback);

        // Check the current network status immediately
        Network activeNetwork = connectivityManager.getActiveNetwork();
        if (activeNetwork != null) {
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork);
            if (networkCapabilities != null) {
                boolean hasInternet = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
                isConnected.postValue(hasInternet);
            }
        } else {
            isConnected.postValue(false); // No active network
        }
    }

    /**
     * Clean up the network callback when the ViewModel is cleared.
     */
    @Override
    protected void onCleared() {
        super.onCleared();
        // Unregister the NetworkCallback to avoid memory leaks
        if (networkCallback != null) {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                connectivityManager.unregisterNetworkCallback(networkCallback);
            }
        }
    }
}