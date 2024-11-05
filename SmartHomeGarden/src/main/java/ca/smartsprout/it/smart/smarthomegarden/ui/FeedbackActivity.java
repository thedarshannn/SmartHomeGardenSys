package ca.smartsprout.it.smart.smarthomegarden.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import ca.smartsprout.it.smart.smarthomegarden.data.model.Feedback;
import ca.smartsprout.it.smart.smarthomegarden.R;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
public class FeedbackActivity extends AppCompatActivity {
    private TextView nameTextView, emailTextView, phoneTextView;
    private RatingBar ratingBar;
    private EditText descriptionEditText;
    private Button submitButton;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Initialize views
        nameTextView = findViewById(R.id.nameTextView);
        emailTextView = findViewById(R.id.EmailfetchtextView2);
        phoneTextView = findViewById(R.id.PhonefetchtextView);
        ratingBar = findViewById(R.id.ratingBar);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        submitButton = findViewById(R.id.feedbackbutton);

        // Fetch user details
        fetchUserDetails();

        // Set up the submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitFeedback();
            }
        });
    }

    private void fetchUserDetails() {
        // Get the current user
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid(); // Get the authenticated user's ID

            // Fetch user data from Firestore
            DocumentReference docRef = firestore.collection("users").document(userId);
            docRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult() != null) {
                    String name = task.getResult().getString("name");
                    String email = task.getResult().getString("email");
                    String phone = task.getResult().getString("phoneNumber");

                    nameTextView.setText(name);
                    emailTextView.setText(email);
                    phoneTextView.setText(phone);
                } else {
                    // Handle case where user data is not found
                    Toast.makeText(this, getString(R.string.faileddata), Toast.LENGTH_SHORT).show();
                    nameTextView.setText("N/A");
                    emailTextView.setText("N/A");
                    phoneTextView.setText("N/A");
                }
            });
        } else {
            // Handle case where the user is not authenticated
            Toast.makeText(this, getString(R.string.loggeddata), Toast.LENGTH_SHORT).show();
            // Redirect to the login activity
            Intent intent = new Intent(FeedbackActivity.this, SettingsActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void submitFeedback() {
        String description = descriptionEditText.getText().toString();
        float rating = ratingBar.getRating();
        if (description.isEmpty()) {
            Toast.makeText(this, getString(R.string.entdesc), Toast.LENGTH_SHORT).show();
            return;
        }
        // Create a feedback object
        Feedback feedback = new Feedback(
                nameTextView.getText().toString(),
                emailTextView.getText().toString(),
                phoneTextView.getText().toString(),
                rating,
                description
        );

        // Store feedback in Firestore
        firestore.collection("feedbacks").add(feedback)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, getString(R.string.feedbacksuccess), Toast.LENGTH_SHORT).show();
                    // Feedback submitted successfully
                    descriptionEditText.setText(""); // Clear the input
                    ratingBar.setRating(0); // Reset rating
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    Toast.makeText(this, getString(R.string.feebackfail), Toast.LENGTH_SHORT).show();
                });
    }
}