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
