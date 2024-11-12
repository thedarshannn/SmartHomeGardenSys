package ca.smartsprout.it.smart.smarthomegarden.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.List;
import ca.smartsprout.it.smart.smarthomegarden.data.model.Notification;

public class NotificationViewModel extends ViewModel {

    private final MutableLiveData<Boolean> notificationPermissionState = new MutableLiveData<>();
    private final MutableLiveData<List<Notification>> notifications = new MutableLiveData<>();

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

    public void addNotification(Notification notification) {
        List<Notification> currentNotifications = notifications.getValue();
        if (currentNotifications != null) {
            currentNotifications.add(notification);
            notifications.setValue(currentNotifications);
        }
    }
}
