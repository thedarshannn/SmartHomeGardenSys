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
import androidx.lifecycle.ViewModel;

import ca.smartsprout.it.smart.smarthomegarden.data.repository.FirebaseRepository;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


import androidx.lifecycle.MutableLiveData;


import ca.smartsprout.it.smart.smarthomegarden.data.model.User;
public class AuthViewModel extends ViewModel {
    private final FirebaseRepository firebaseRepository ;
    private MutableLiveData<Boolean> isLoggedIn = new MutableLiveData<>();
    private LiveData<Boolean> isResetEmailSent;
    private LiveData<String> resetEmailError;
    public AuthViewModel() {
        firebaseRepository = new FirebaseRepository();
        isResetEmailSent = firebaseRepository.getIsResetEmailSent();
        resetEmailError = firebaseRepository.getResetEmailError();
    }



    public LiveData<Boolean> getLoginStatus() {
        return isLoggedIn;
    }
    public LiveData<AuthResult> loginUser(String email, String password) {
        return firebaseRepository.loginUser(email, password);
    }
    public void checkLoggedInStatus() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            isLoggedIn.setValue(true);  // User is logged in
        } else {
            isLoggedIn.setValue(false); // No user logged in
        }
    }

    public LiveData<AuthResult> registerUser(String email, String password) {
        return firebaseRepository.registerUser(email, password);
    }
    public LiveData<Boolean> saveUserDataToFirestore(String uid, User user) {
        return firebaseRepository.saveUserDataToFirestore(uid, user);
    }

    // Validation methods
    public boolean isValidEmail(String email) {
        return firebaseRepository.isValidEmail(email);
    }

    public boolean isValidPassword(String password) {
        return firebaseRepository.isValidPassword(password);
    }

    // Expose the method to send password reset email
    public void sendPasswordResetEmail(String email) {
        firebaseRepository.sendPasswordResetEmail(email);
    }

    // Expose LiveData for reset email status
    public LiveData<Boolean> getIsResetEmailSent() {
        return isResetEmailSent;
    }

    // Expose LiveData for reset email error
    public LiveData<String> getResetEmailError() {
        return resetEmailError;
    }

    // Method to update Firestore after password change
    public void updatePasswordChangeTimestamp() {
        firebaseRepository.updatePasswordChangeTimestamp();
    }

}
