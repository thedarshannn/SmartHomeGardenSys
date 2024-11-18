package ca.smartsprout.it.smart.smarthomegarden.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PasswordViewModel extends ViewModel {

    private final MutableLiveData<Boolean> currentPasswordValidation = new MutableLiveData<>();
    private final MutableLiveData<Boolean> passwordsMatch = new MutableLiveData<>();
    private final MutableLiveData<Boolean> updateStatus = new MutableLiveData<>();
    private final FirebaseAuth firebaseAuth;

    public PasswordViewModel() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public LiveData<Boolean> getCurrentPasswordValidation() {
        return currentPasswordValidation;
    }

    public LiveData<Boolean> getPasswordsMatch() {
        return passwordsMatch;
    }

    public LiveData<Boolean> getUpdateStatus() {
        return updateStatus;
    }

    public void validateCurrentPassword(String currentPassword) {
        if (currentPassword == null || currentPassword.trim().isEmpty()) {
            currentPasswordValidation.setValue(false); // Notify invalid input
            return;
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);

            // Log details for debugging
            Log.d("ValidatePassword", "Email: " + user.getEmail() + ", CurrentPassword: " + currentPassword);

            user.reauthenticate(credential)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("Reauthenticate", "Reauthentication successful");
                            currentPasswordValidation.setValue(true); // Notify successful validation
                        } else {
                            Log.e("Reauthenticate", "Failed to reauthenticate: " + task.getException().getMessage());
                            currentPasswordValidation.setValue(false);
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Reauthenticate", "Error during reauthentication: " + e.getMessage());
                        currentPasswordValidation.setValue(false);
                    });
        } else {
            Log.e("ValidatePassword", "No user is currently signed in.");
            currentPasswordValidation.setValue(false); // Notify no user logged in
        }
    }





    public void checkPasswordsMatch(String newPassword, String retypePassword) {
        passwordsMatch.setValue(newPassword.equals(retypePassword));
    }

    public void updatePassword(String currentPassword, String newPassword) {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null) {
            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);

            user.reauthenticate(credential)
                    .addOnCompleteListener(reauthTask -> {
                        if (reauthTask.isSuccessful()) {
                            user.updatePassword(newPassword)
                                    .addOnCompleteListener(updateTask -> {
                                        if (updateTask.isSuccessful()) {
                                            updateStatus.setValue(true);
                                            Log.d("UpdatePassword", "Password updated successfully");
                                        } else {
                                            updateStatus.setValue(false);
                                            Log.e("UpdatePassword", "Failed to update password: " + updateTask.getException().getMessage());
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        updateStatus.setValue(false);
                                        Log.e("UpdatePassword", "Error updating password: " + e.getMessage());
                                    });
                        } else {
                            updateStatus.setValue(false);
                            Log.e("Reauthenticate", "Failed to reauthenticate: " + reauthTask.getException().getMessage());
                        }
                    })
                    .addOnFailureListener(e -> {
                        updateStatus.setValue(false);
                        Log.e("Reauthenticate", "Error during reauthentication: " + e.getMessage());
                    });
        } else {
            updateStatus.setValue(false);
            Log.e("UpdatePassword", "No user is currently signed in.");
        }
    }

    public void handleEmptyPassword() {
        currentPasswordValidation.setValue(false); // Update MutableLiveData value
    }

}
