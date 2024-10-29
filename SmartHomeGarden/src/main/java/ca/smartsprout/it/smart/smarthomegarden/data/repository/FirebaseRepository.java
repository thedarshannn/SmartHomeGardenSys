/**
 * Smart Sprout
 * Members:
 * 1. Aditi Patel, n01525570, CENG322-RCB
 * 2. Birava Prajapati, n01579924, CENG322-RCA
 * 3. Darshankumar Prajapati, n01584247, CENG322-RCB
 * 4. Zeel Patel, n01526282, CENG322-RCB
 */
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