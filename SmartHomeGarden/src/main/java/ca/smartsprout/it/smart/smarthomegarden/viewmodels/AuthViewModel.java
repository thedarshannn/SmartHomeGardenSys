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

public class AuthViewModel extends ViewModel {
    private final FirebaseRepository firebaseRepository = new FirebaseRepository();

    public LiveData<AuthResult> loginUser(String email, String password) {
        return firebaseRepository.loginUser(email, password);
    }

    public LiveData<AuthResult> registerUser(String email, String password) {
        return firebaseRepository.registerUser(email, password);
    }
}
