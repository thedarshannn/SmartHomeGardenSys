package ca.smartsprout.it.smart.smarthomegarden.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.FeedbackViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class FeedbackActivity extends AppCompatActivity {
    private TextView nameTextView, emailTextView, phoneTextView;
    private RatingBar ratingBar;
    private EditText descriptionEditText;
    private Button submitButton;
    private FeedbackViewModel feedbackViewModel;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        // Initialize ViewModel
        feedbackViewModel = new ViewModelProvider(this).get(FeedbackViewModel.class);

        // Initialize views
        nameTextView = findViewById(R.id.nameTextView);
        emailTextView = findViewById(R.id.EmailfetchtextView2);
        phoneTextView = findViewById(R.id.PhonefetchtextView);
        ratingBar = findViewById(R.id.ratingBar);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        submitButton = findViewById(R.id.feedbackbutton);
        progressBar = findViewById(R.id.progressBar);


        // Observe LiveData for user details and feedback status
        feedbackViewModel.getUserDetails().observe(this, user -> {
            if (user != null) {
                nameTextView.setText(user.getName());
                emailTextView.setText(user.getEmail());
                phoneTextView.setText(user.getPhone());
            } else {
                nameTextView.setText("N/A");
                emailTextView.setText("N/A");
                phoneTextView.setText("N/A");
            }
        });

        feedbackViewModel.getFeedbackStatus().observe(this, status -> {
            Toast.makeText(this, status, Toast.LENGTH_SHORT).show();
            if (status.equals(getString(R.string.feedbacksuccess))) {
                // Clear the input fields
                clearInputFields();
                navigateToSettings();
            }
        });

        // Fetch user details when the activity starts
        feedbackViewModel.fetchUserDetails();

        // Set up submit button to submit feedback
        submitButton.setOnClickListener(v -> submitFeedback());
    }

//    private void submitFeedback() {
//        String description = descriptionEditText.getText().toString();
//        float rating = ratingBar.getRating();
//
//        if (description.isEmpty()) {
//            Toast.makeText(this, getString(R.string.entdesc), Toast.LENGTH_SHORT).show();
//        } else {
//            feedbackViewModel.submitFeedback(
//                    nameTextView.getText().toString(),
//                    emailTextView.getText().toString(),
//                    phoneTextView.getText().toString(),
//                    rating,
//                    description
//            );
//        }
//    }
private void submitFeedback() {
    String description = descriptionEditText.getText().toString();
    float rating = ratingBar.getRating();

    if (description.isEmpty()) {
        Toast.makeText(this, getString(R.string.entdesc), Toast.LENGTH_SHORT).show();
    } else {
        // Show progress bar
        progressBar.setVisibility(View.VISIBLE);

        // Delay the submission by 5 seconds
        new Handler().postDelayed(() -> {
            feedbackViewModel.submitFeedback(
                    nameTextView.getText().toString(),
                    emailTextView.getText().toString(),
                    phoneTextView.getText().toString(),
                    rating,
                    description
            );
            // Hide progress bar
            progressBar.setVisibility(View.GONE);
        }, 5000);
    }
}
    private void clearInputFields() {
        descriptionEditText.setText(""); // Clear the description input
        ratingBar.setRating(0); // Reset the rating
    }
    private void navigateToSettings() {
        // Redirect the user to the SettingsActivity
        Intent intent = new Intent(FeedbackActivity.this, SettingsActivity.class);
        startActivity(intent);
         // Optional: finish the current activity to prevent it from appearing in the back stack
    }

}