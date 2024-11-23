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

import android.util.Patterns;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import ca.smartsprout.it.smart.smarthomegarden.data.model.Feedback;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ca.smartsprout.it.smart.smarthomegarden.data.model.User;

public class FirebaseRepository {
    private final FirebaseFirestore firestore;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private MutableLiveData<Boolean> isResetEmailSent;
    private MutableLiveData<String> resetEmailError;

    public FirebaseRepository() {
        firestore = FirebaseFirestore.getInstance();

        isResetEmailSent = new MutableLiveData<>();
        resetEmailError = new MutableLiveData<>();
    }


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

    // Method for saving user profile data to Firestore
    public LiveData<Boolean> saveUserDataToFirestore(String uid, User user) {
        MutableLiveData<Boolean> saveResult = new MutableLiveData<>();

        db.collection("users").document(uid)
                .set(user)
                .addOnSuccessListener(aVoid -> saveResult.setValue(true))
                .addOnFailureListener(e -> saveResult.setValue(false));

        return saveResult;
    }

    // Validation method for email
    public boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Validation method for password
    public boolean isValidPassword(String password) {
        return password.length() <= 10;
    }

    // Fetch user details
    // Fetch user details
    public void fetchUserDetails(String userId, OnUserFetchedListener listener) {
        DocumentReference docRef = firestore.collection("users").document(userId);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String name = documentSnapshot.getString("name");
                String email = documentSnapshot.getString("email");
                String phone = documentSnapshot.getString("phoneNumber");
                listener.onUserFetched(new Feedback(name, email, phone, 0, ""));
            } else {
                listener.onUserFetched(null);
            }
        });
    }

    // Submit feedback to Firestore
    public void submitFeedback(Feedback feedback, OnFeedbackSubmissionListener listener) {
        // Add device model to the feedback before submitting
        String deviceModel = getDeviceModel(); // Retrieve device model
        feedback.setDeviceModel(deviceModel); // Set the device model in the feedback

        firestore.collection("feedbacks").add(feedback)
                .addOnSuccessListener(documentReference -> listener.onFeedbackSubmitted(true))
                .addOnFailureListener(e -> listener.onFeedbackSubmitted(false));
    }

    // Fetch user details
    public LiveData<User> fetchUserDetails() {
        MutableLiveData<User> userDetails = new MutableLiveData<>();
        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        firestore.collection("users").document(userId)
                .addSnapshotListener((documentSnapshot, e) -> {
                    if (e != null) {
                        // Log error
                        return;
                    }
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);
                        userDetails.setValue(user); // Notify observers of the change
                    }
                });

        return userDetails;
    }
    public LiveData<String> fetchUserEmail(String userId) {
        MutableLiveData<String> emailLiveData = new MutableLiveData<>();

        firestore.collection("users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String email = documentSnapshot.getString("email");
                        emailLiveData.setValue(email); // Pass the email to LiveData
                    } else {
                        emailLiveData.setValue(null); // Handle no document case
                    }
                })
                .addOnFailureListener(e -> {
                    emailLiveData.setValue(null); // Handle failure case
                });

        return emailLiveData;
    }

    // Update user name in Firebase
    public void updateUserName(String newName) {
        String userId = mAuth.getCurrentUser().getUid();
        firestore.collection("users").document(userId)
                .update("name", newName);
    }

    // Method to retrieve device model programmatically
    private String getDeviceModel() {
        return android.os.Build.MODEL; // Get the device model
    }

    public interface OnUserFetchedListener {
        void onUserFetched(Feedback user);
    }

    public interface OnFeedbackSubmissionListener {
        void onFeedbackSubmitted(boolean isSuccess);
    }


    // Method to send password reset email
    public void sendPasswordResetEmail(String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        isResetEmailSent.setValue(true);
                    } else {
                        isResetEmailSent.setValue(false);
                        if (task.getException() != null) {
                            resetEmailError.setValue(task.getException().getMessage());
                        }
                    }
                });
    }

    // LiveData for observing the email reset status
    public LiveData<Boolean> getIsResetEmailSent() {
        return isResetEmailSent;
    }

    // LiveData for observing errors during reset
    public LiveData<String> getResetEmailError() {
        return resetEmailError;
    }

    // Method to update Firestore when the password changes
    public void updatePasswordChangeTimestamp() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            Map<String, Object> update = new HashMap<>();
            update.put("passwordUpdatedAt", System.currentTimeMillis());


            firestore.collection("users").document(userId)
                    .update(update)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Handle success if needed
                        } else {
                            // Handle failure if needed
                        }
                    });
        }

    }
}