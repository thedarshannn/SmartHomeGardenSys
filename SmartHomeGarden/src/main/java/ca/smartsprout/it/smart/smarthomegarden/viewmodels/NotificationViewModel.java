/**
 * Smart Sprout
 * Members:
 * 1. Aditi Patel, n01525570, CENG322-RCB
 * 2. Birava Prajapati, n01579924, CENG322-RCA
 * 3. Darshankumar Prajapati, n01574247, CENG322-RCB
 * 4. Zeel Patel, n01526282, CENG322-RCB
 */

package ca.smartsprout.it.smart.smarthomegarden.viewmodels;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import ca.smartsprout.it.smart.smarthomegarden.data.model.Notification;

public class NotificationViewModel extends ViewModel {
    private final MutableLiveData<Boolean> notificationPermissionState = new MutableLiveData<>();
    private final MutableLiveData<List<Notification>> notifications = new MutableLiveData<>(new ArrayList<>());

    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    public NotificationViewModel() {
        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Get current user
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("notifications");
        }
    }

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
    public void addNotification(String title, String message) {
        String id = databaseReference.push().getKey();
        long timestamp = System.currentTimeMillis();
        Notification notification = new Notification(id, title, message, timestamp);
        if (id != null) {
            databaseReference.child(id).setValue(notification)
                    .addOnSuccessListener(aVoid -> Log.d("NotificationActivity", "Notification added successfully"))
                    .addOnFailureListener(e -> Log.e("NotificationActivity", "Failed to add notification", e));
        }
    }

}
