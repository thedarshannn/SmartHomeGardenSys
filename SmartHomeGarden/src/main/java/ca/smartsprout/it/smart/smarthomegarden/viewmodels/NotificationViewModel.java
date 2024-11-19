package ca.smartsprout.it.smart.smarthomegarden.viewmodels;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import ca.smartsprout.it.smart.smarthomegarden.data.model.Notification;

public class NotificationViewModel extends ViewModel {
    private final MutableLiveData<Boolean> notificationPermissionState = new MutableLiveData<>();
    private final MutableLiveData<List<Notification>> notifications = new MutableLiveData<>(new ArrayList<>());

    public void updateNotificationPermission(boolean isGranted) {
        notificationPermissionState.setValue(isGranted);
    }

    public LiveData<Boolean> getNotificationPermissionState() {
        return notificationPermissionState;
    }

    public LiveData<List<Notification>> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notificationList) {
        notifications.setValue(notificationList);
    }
    public boolean isNotificationPermissionGranted(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                    == PackageManager.PERMISSION_GRANTED;
        }
        return true; // Notifications are always enabled for older versions
    }


    public void addNotification(Notification notification) {
        List<Notification> currentNotifications = notifications.getValue();
        if (currentNotifications != null) {
            currentNotifications.add(notification);
            notifications.setValue(currentNotifications);
        }
    }
}
