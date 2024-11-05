/**
 * Smart Sprout
 * Members:
 * 1. Aditi Patel, n01525570, CENG322-RCB
 * 2. Birava Prajapati, n01579924, CENG322-RCA
 * 3. Darshankumar Prajapati, n01584247, CENG322-RCB
 * 4. Zeel Patel, n01526282, CENG322-RCB
 */
package ca.smartsprout.it.smart.smarthomegarden.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NotificationViewModel extends ViewModel {

    private final MutableLiveData<Boolean> notificationPermissionState = new MutableLiveData<>();

    public void updateNotificationPermission(boolean isGranted) {
        notificationPermissionState.setValue(isGranted);
    }

    public LiveData<Boolean> getNotificationPermissionState() {
        return notificationPermissionState;
    }
}
