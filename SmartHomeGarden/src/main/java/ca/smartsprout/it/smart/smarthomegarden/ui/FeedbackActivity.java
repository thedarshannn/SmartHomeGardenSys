/**
 * Smart Sprout
 * Members:
 * 1. Aditi Patel, n01525570, CENG322-RCB
 * 2. Birava Prajapati, n01579924, CENG322-RCA
 * 3. Darshankumar Prajapati, n01574247, CENG322-RCB
 * 4. Zeel Patel, n01526282, CENG322-RCB
 */

package ca.smartsprout.it.smart.smarthomegarden.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.FeedbackViewModel;
import android.graphics.Color;


import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FeedbackActivity extends AppCompatActivity {
    public TextView nameTextView;
    public TextView emailTextView;
    public TextView phoneTextView;
    public TextView timerTextView;
    public RatingBar ratingBar;
    public EditText descriptionEditText;
    private Button submitButton;
    private FeedbackViewModel feedbackViewModel;
    private ProgressBar progressBar;

    private static final long SUBMISSION_INTERVAL = 24 * 60 * 60 * 1000; // 24 hours in milliseconds
    public SharedPreferences sharedPreferences;
    public Handler handler = new Handler();
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        // Initialize ViewModel
        feedbackViewModel = new ViewModelProvider(this).get(FeedbackViewModel.class);


        // Initialize ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Handle the back button press
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish(); // Close the activity and go back
            }
        });

        // Initialize views
        nameTextView = findViewById(R.id.nameTextView);
        emailTextView = findViewById(R.id.EmailfetchtextView2);
        phoneTextView = findViewById(R.id.PhonefetchtextView);
        ratingBar = findViewById(R.id.ratingBar);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        submitButton = findViewById(R.id.feedbackbutton);
        progressBar = findViewById(R.id.progressBar);
        timerTextView = findViewById(R.id.timerTextView);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(getString(R.string.feedbackprefs), MODE_PRIVATE);

        // Get the current user's email
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            userEmail = currentUser.getEmail();
        }

        // Observe LiveData for user details and feedback status
        feedbackViewModel.getUserDetails().observe(this, user -> {
            if (user != null) {
                nameTextView.setText(user.getName());
                emailTextView.setText(user.getEmail());
                phoneTextView.setText(user.getPhone());
            } else {
                nameTextView.setText(R.string.n_a);
                emailTextView.setText(R.string.n_a);
                phoneTextView.setText(R.string.n_a);
            }
        });

        feedbackViewModel.getFeedbackStatus().observe(this, status -> {
            Toast.makeText(this, status, Toast.LENGTH_SHORT).show();
            if (status.equals(getString(R.string.feedbacksuccess))) {
                // Clear the input fields
                clearInputFields();
                navigateToSettings();

                // Show the confirmation dialog
                showConfirmationDialog();
            }
        });

        // Fetch user details when the activity starts
        feedbackViewModel.fetchUserDetails();

        // Set up submit button to submit feedback
        submitButton.setOnClickListener(v -> submitFeedback());

        // Check if the user is within the submission interval
        checkSubmissionInterval();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Close the activity and go back
        return true;
    }


    private void submitFeedback() {
        String description = descriptionEditText.getText().toString();
        float rating = ratingBar.getRating();

        if (description.isEmpty()) {
            Toast.makeText(this, getString(R.string.entdesc), Toast.LENGTH_SHORT).show();
        } else {
            long lastSubmissionTime = sharedPreferences.getLong(userEmail, 0);
            long currentTime = System.currentTimeMillis();

            if (currentTime - lastSubmissionTime < SUBMISSION_INTERVAL) {
                Toast.makeText(this, getString(R.string.submission_limit), Toast.LENGTH_SHORT).show();
            } else {
                // Show progress bar
                progressBar.setVisibility(View.VISIBLE);

                // Delay the submission by 5 seconds
                new Handler().postDelayed(() -> {
                    // Submit the feedback
                    feedbackViewModel.submitFeedback(
                            nameTextView.getText().toString(),
                            emailTextView.getText().toString(),
                            phoneTextView.getText().toString(),
                            rating,
                            description
                    );

                    // Hide progress bar
                    progressBar.setVisibility(View.GONE);

                    // Save the current submission time
                    sharedPreferences.edit().putLong(userEmail, currentTime).apply();

                    // Clear the input fields
                    clearInputFields();

                    // Disable input fields and submit button
                    setFieldsEnabled(false);

                    // Show the confirmation dialog
                    showConfirmationDialog();
                }, 5000);
            }
        }
    }


    public void clearInputFields() {
        descriptionEditText.setText(""); // Clear the description input
        ratingBar.setRating(0); // Reset the rating
    }

    private void navigateToSettings() {
        // Redirect the user to the SettingsActivity
        Intent intent = new Intent(FeedbackActivity.this, SettingsActivity.class);
        startActivity(intent);
        // Optional: finish the current activity to prevent it from appearing in the back stack
    }

    public void setFieldsEnabled(boolean enabled) {
        nameTextView.setEnabled(enabled);
        emailTextView.setEnabled(enabled);
        phoneTextView.setEnabled(enabled);
        ratingBar.setEnabled(enabled);
        descriptionEditText.setEnabled(enabled);
        submitButton.setEnabled(enabled);

        int color = enabled ? Color.BLACK : Color.GRAY;
        nameTextView.setTextColor(color);
        emailTextView.setTextColor(color);
        phoneTextView.setTextColor(color);
        descriptionEditText.setTextColor(color);

        float alpha = enabled ? 1.0f : 0.5f;
        ratingBar.setAlpha(alpha);
        descriptionEditText.setAlpha(alpha);
        submitButton.setAlpha(alpha);
    }

    public void checkSubmissionInterval() {
        long lastSubmissionTime = sharedPreferences.getLong(userEmail, 0);
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastSubmissionTime < SUBMISSION_INTERVAL) {
            setFieldsEnabled(false);
            startCountdownTimer(SUBMISSION_INTERVAL - (currentTime - lastSubmissionTime));
        }
    }

    private void startCountdownTimer(long duration) {
        timerTextView.setVisibility(View.VISIBLE);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();
                long remainingTime = duration - (currentTime - sharedPreferences.getLong(userEmail, 0));

                if (remainingTime > 0) {
                    long hours = remainingTime / (60 * 60 * 1000);
                    long minutes = (remainingTime / (60 * 1000)) % 60;
                    long seconds = (remainingTime / 1000) % 60;

                    String timeString = String.format(getString(R.string.next_feedback), hours, minutes, seconds);
                    timerTextView.setText(timeString);

                    handler.postDelayed(this, 1000);
                } else {
                    setFieldsEnabled(true);
                    timerTextView.setText("");
                    timerTextView.setVisibility(View.GONE);
                }
            }
        }, 1000);
    }
    private void showConfirmationDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.feedback_submitted)
                .setIcon(R.drawable.ic_approve)
                .setMessage(getString(R.string.thank_you_for_your_feedback_your_submission_has_been_successfully_recorded))
                .setPositiveButton(R.string.ok, (dialog, which) -> dialog.dismiss())
                .setCancelable(false)
                .show();
    }

}
