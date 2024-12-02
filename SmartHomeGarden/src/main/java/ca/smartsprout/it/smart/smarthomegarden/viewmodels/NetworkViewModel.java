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
import android.net.ConnectivityManager;
import android.net.Network;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

/**
 * ViewModel class to observe the network status. <br>
 * This class is responsible for observing the network status and updating the UI accordingly.
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
     * @return LiveData object that represents the network status.
     */
    public LiveData<Boolean> getIsConnected() {
        return isConnected;
    }


    /**
     * Observe the network status.
     * @param context The context of the application.
     */
    private void observeNetwork(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
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
            };

            connectivityManager.registerDefaultNetworkCallback(networkCallback);
        }
    }

    /**
     * Clean up the network callback when the ViewModel is cleared.
     */
    @Override
    protected void onCleared() {
        super.onCleared();
        // Clean up the network callback
        if (networkCallback != null) {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                connectivityManager.unregisterNetworkCallback(networkCallback);
            }
        }
    }
}
