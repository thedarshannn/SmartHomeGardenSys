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
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;

import ca.smartsprout.it.smart.smarthomegarden.data.model.User;
import ca.smartsprout.it.smart.smarthomegarden.data.repository.FirebaseRepository;


public class UserViewModel extends ViewModel {
    public FirebaseRepository firebaseRepository;
    private final MutableLiveData<String> userName;

    public UserViewModel() {
        firebaseRepository = new FirebaseRepository();
        userName = new MutableLiveData<>();

        // Fetch user details from Firebase
        firebaseRepository.fetchUserDetails().observeForever(user -> {
            if (user != null) {
                userName.setValue(user.getName());
            }
        });
    }

    // Constructor for testing
    public UserViewModel(FirebaseRepository firebaseRepository) {
        this.firebaseRepository = firebaseRepository;
        this.userName = new MutableLiveData<>();

        // Fetch user details from Firebase
        LiveData<User> userDetails = this.firebaseRepository.fetchUserDetails();
        if (userDetails != null) {
            userDetails.observeForever(user -> {
                if (user != null) {
                    userName.setValue(user.getName());
                }
            });
        }
    }



    public LiveData<String> getUserName() {
        return userName;
    }
    public LiveData<String> getUserEmail() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            return firebaseRepository.fetchUserEmail(userId);
        }
        return null;
    }


    public void updateUserName(String newName) {
        firebaseRepository.updateUserName(newName); // Update name in Firebase
        userName.setValue(newName); // Reflect change locally
    }

    public String getUserId() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            return FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        return null;
    }
}
