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
import ca.smartsprout.it.smart.smarthomegarden.data.model.Feedback;
import ca.smartsprout.it.smart.smarthomegarden.data.repository.FirebaseRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FeedbackViewModel extends ViewModel {
    private final FirebaseRepository firebaseRepository;
    private final MutableLiveData<Feedback> userDetails = new MutableLiveData<>();
    private final MutableLiveData<String> feedbackStatus = new MutableLiveData<>();

    public FeedbackViewModel() {
        firebaseRepository = new FirebaseRepository();
    }

    // LiveData getters
    public LiveData<Feedback> getUserDetails() {
        return userDetails;
    }

    public LiveData<String> getFeedbackStatus() {
        return feedbackStatus;
    }

    // Fetch user details using FirebaseAuth
    public void fetchUserDetails() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            firebaseRepository.fetchUserDetails(userId, user -> {
                if (user != null) {
                    userDetails.setValue(user);
                } else {
                    feedbackStatus.setValue("User data not found.");
                }
            });
        } else {
            feedbackStatus.setValue("User not authenticated");
        }
    }

    // Submit feedback to Firestore
    public void submitFeedback(String name, String email, String phone, float rating, String description) {
        // Create Feedback object with the device model
        Feedback feedback = new Feedback(name, email, phone, rating, description);
        feedback.setDeviceModel(getDeviceModel()); // Set device model

        firebaseRepository.submitFeedback(feedback, isSuccess -> {
            feedbackStatus.setValue(isSuccess ? "Feedback submitted successfully" : "Failed to submit feedback");
        });
    }

    // Method to retrieve device model programmatically
    private String getDeviceModel() {
        return android.os.Build.MODEL; // Get the device model
    }
}
