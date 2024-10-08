package ca.smartsprout.it.smart.smarthomegarden.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class FirebaseRepository {
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public LiveData<AuthResult> loginUser(String email, String password) {
        MutableLiveData<AuthResult> loginResult = new MutableLiveData<>();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        loginResult.setValue(task.getResult());
                    } else {
                        loginResult.setValue(null);
                    }
                });

        return loginResult;
    }

    public LiveData<AuthResult> registerUser(String email, String password) {
        MutableLiveData<AuthResult> registerResult = new MutableLiveData<>();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        registerResult.setValue(task.getResult());
                    } else {
                        registerResult.setValue(null);
                    }
                });

        return registerResult;
    }
}