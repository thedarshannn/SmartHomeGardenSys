package ca.smartsprout.it.smart.smarthomegarden.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import ca.smartsprout.it.smart.smarthomegarden.utils.GoogleSignin.GoogleSignInHelper;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ca.smartsprout.it.smart.smarthomegarden.MainActivity;
import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.data.model.Notification;
import ca.smartsprout.it.smart.smarthomegarden.utils.NotificationHelper;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.AuthViewModel;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class LoginActivity extends AppCompatActivity {
    private EditText emailInput, passwordInput;
    private Button loginButton,googlesignin;
    private AuthViewModel authViewModel;

private TextView registerswitch,forgotpassword;
    private GoogleSignInHelper googleSignInHelpers;


    private CheckBox rememberMeCheckbox;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI elements
        emailInput = findViewById(R.id.editTextEmail);
        passwordInput = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.button);
        registerswitch = findViewById(R.id.registerswitch);
        rememberMeCheckbox = findViewById(R.id.rememberMeCheckbox);
        forgotpassword=findViewById(R.id.forgotPassword);
        googlesignin=findViewById(R.id.googlesignin);
        googleSignInHelpers = new GoogleSignInHelper(this);

        // Hide the Toolbar for this activity
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        // Initialize ViewModel
        passwordInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        emailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (!Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                    emailInput.setError(getString(R.string.invalidemail));
                }
            }
        });


        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        databaseReference = FirebaseDatabase.getInstance().getReference(getString(R.string.notifications));

        registerswitch.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(intent);
        });




        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        int status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        Log.d("GooglePlayServices", "Google Play Services status: " + status);
        googlesignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Log.d("RegistrationActivity", "Google Sign-In button clicked");

                if (!googleSignInHelpers.isSignedIn()) {
                    // Trigger Google Sign-In
                    googleSignInHelpers.signIn(result -> {
                       Log.d("RegistrationActivity", "Sign-In result: " + result); // Log the result
                        if (result) {
                            // Handle successful sign-in
                            runOnUiThread(() -> {
                                Toast.makeText(LoginActivity.this, getString(R.string.login), Toast.LENGTH_SHORT).show();
                                // Proceed to the next activity or update the UI
                                goToHomeScreen();
                            });
                        } else {
                            // Handle sign-in failure
                            runOnUiThread(() -> {
                                Toast.makeText(LoginActivity.this, getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
                            });
                        }
                        return null;
                    });
                } else {
                    // Already signed in, proceed to the next activity or handle accordingly
                    Toast.makeText(LoginActivity.this, getString(R.string.signedin), Toast.LENGTH_SHORT).show();
                    goToHomeScreen();
                }
            }
        });

// Observe the login status
        authViewModel.getLoginStatus().observe(this, loggedIn -> {
            if (loggedIn) {
                goToHomeScreen();
            }
        });

        // Check if user is already logged in
        authViewModel.checkLoggedInStatus();
        // Set click listener for login button
        loginButton.setOnClickListener(v -> loginUser());

        forgotpassword.setOnClickListener(v -> showForgotPasswordDialog());

        // Observe the LiveData from ViewModel
        observeViewModel();
    }


    private void goToHomeScreen() {
        // Example: Navigate to HomeActivity after successful sign-in
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }





    private void loginUser() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, getString(R.string.email_password), Toast.LENGTH_SHORT).show();
        } else if (password.length() > 10) {
            passwordInput.setError(getString(R.string.exceed));
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError(getString(R.string.invalidemail));
        } else {

            authViewModel.loginUser(email, password).observe(this, this::handleLoginResult);
        }
    }

    private void handleLoginResult(@Nullable AuthResult authResult) {
        if (authResult != null) {
            Toast.makeText(this, getString(R.string.login), Toast.LENGTH_SHORT).show();
            showLoginNotification();
            onLoginSuccess();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
        }
    }

    private void showLoginNotification() {
        // Generate a unique ID for the notification
        String notificationId = databaseReference.push().getKey();

        // Get the current timestamp
        long timestamp = System.currentTimeMillis();

        // Create the notification object
        Notification notification = new Notification(notificationId, getString(R.string.login_successful), getString(R.string.welcome_back), timestamp);

        // Save the notification to the database
        if (notificationId != null) {
            databaseReference.child(notificationId).setValue(notification);
        }
    }
    private void onLoginSuccess() {
        // Show login notification
        NotificationHelper.createNotification(this, getString(R.string.login_successful), getString(R.string.welcome_back));
    }

    private void showForgotPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.resetpassword);

        final EditText inputEmail = new EditText(this);
        inputEmail.setHint(R.string.enter_registered_email);
        inputEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        builder.setView(inputEmail);

        builder.setPositiveButton(R.string.send, (dialog, which) -> {
            String email = inputEmail.getText().toString().trim();
            if (!TextUtils.isEmpty(email)) {
                authViewModel.sendPasswordResetEmail(email);
            } else {
                Toast.makeText(this, R.string.valid, Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void observeViewModel() {
        authViewModel.getIsResetEmailSent().observe(this, isSent -> {
            if (isSent) {
                Toast.makeText(this, R.string.reset_email_sent, Toast.LENGTH_LONG).show();
                // Update Firestore with password change timestamp
                authViewModel.updatePasswordChangeTimestamp();

            } else {
                Toast.makeText(this, R.string.failed_to_send_email, Toast.LENGTH_LONG).show();
            }
        });

        authViewModel.getResetEmailError().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, "Error: " + error, Toast.LENGTH_LONG).show();
            }
        });
    }
}
