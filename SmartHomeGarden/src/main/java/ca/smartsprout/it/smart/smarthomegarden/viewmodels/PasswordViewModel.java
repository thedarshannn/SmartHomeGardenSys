/**
 * Smart Sprout
 * Members:
 * 1. Aditi Patel, n01525570, CENG322-RCB
 * 2. Birava Prajapati, n01579924, CENG322-RCA
 * 3. Darshankumar Prajapati, n01574247, CENG322-RCB
 * 4. Zeel Patel, n01526282, CENG322-RCB
 */

package ca.smartsprout.it.smart.smarthomegarden.viewmodels;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PasswordViewModel extends ViewModel {

    private final MutableLiveData<Boolean> currentPasswordValidation = new MutableLiveData<>();
    private final MutableLiveData<Boolean> passwordsMatch = new MutableLiveData<>();
    private final MutableLiveData<Boolean> updateStatus = new MutableLiveData<>();
    private final MutableLiveData<Boolean> passwordRegexValidation = new MutableLiveData<>();
    public FirebaseAuth firebaseAuth;
    private static final String PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$";

    public PasswordViewModel() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    // Constructor for testing
    public PasswordViewModel(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    public LiveData<Boolean> getCurrentPasswordValidation() {
        return currentPasswordValidation;
    }

    public LiveData<Boolean> getPasswordsMatch() {
        return passwordsMatch;
    }

    public LiveData<Boolean> getPasswordRegexValidation() {
        return passwordRegexValidation;
    }

    private void validateRegex(String newPassword) {
        passwordRegexValidation.setValue(newPassword.matches(PASSWORD_PATTERN));
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
            AuthCredential credential = EmailAuthProvider.getCredential(Objects.requireNonNull(user.getEmail()), currentPassword);

            user.reauthenticate(credential)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {

                            currentPasswordValidation.setValue(true); // Notify successful validation
                        } else {

                            currentPasswordValidation.setValue(false);
                        }
                    })
                    .addOnFailureListener(e -> {
                        currentPasswordValidation.setValue(false);
                    });
        } else {
            currentPasswordValidation.setValue(false); // Notify no user logged in
        }
    }





    public void checkPasswordsMatch(String newPassword, String retypePassword) {
        passwordsMatch.setValue(newPassword.equals(retypePassword));
    }

    public void updatePassword(String currentPassword, String newPassword) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        if (user == null) {
            updateStatus.setValue(false); // User is not logged in
            return;
        }

        AuthCredential credential = EmailAuthProvider.getCredential(Objects.requireNonNull(user.getEmail()), currentPassword);

        user.reauthenticate(credential).addOnCompleteListener(reauthTask -> {
            if (!reauthTask.isSuccessful()) {
                updateStatus.setValue(false);
                return;
            }

            user.updatePassword(newPassword).addOnCompleteListener(updateTask -> {
                if (!updateTask.isSuccessful()) {
                    updateStatus.setValue(false);
                    return;
                }

                // Update Firestore with both password and confirmPassword fields
                Map<String, Object> updates = new HashMap<>();
                updates.put("password", newPassword);
                updates.put("confirmPassword", newPassword);

                firestore.collection("users").document(user.getUid())
                        .update(updates)
                        .addOnCompleteListener(firestoreTask -> updateStatus.setValue(firestoreTask.isSuccessful()))
                        .addOnFailureListener(e -> updateStatus.setValue(false));
            }).addOnFailureListener(e -> updateStatus.setValue(false));
        }).addOnFailureListener(e -> updateStatus.setValue(false));
    }



    public void handleEmptyPassword() {
        currentPasswordValidation.setValue(false); // Update MutableLiveData value
    }

    public String getPasswordPattern() {
        return PASSWORD_PATTERN;

    }
}
