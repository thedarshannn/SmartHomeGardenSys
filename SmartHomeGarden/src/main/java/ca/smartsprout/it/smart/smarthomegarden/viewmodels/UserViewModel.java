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

import ca.smartsprout.it.smart.smarthomegarden.data.repository.FirebaseRepository;


public class UserViewModel extends ViewModel {
    private FirebaseRepository firebaseRepository;
    private MutableLiveData<String> userName;

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

    public LiveData<String> getUserName() {
        return userName;
    }

    public void updateUserName(String newName) {
        firebaseRepository.updateUserName(newName); // Update name in Firebase
        userName.setValue(newName); // Reflect change locally
    }
}
