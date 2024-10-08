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
