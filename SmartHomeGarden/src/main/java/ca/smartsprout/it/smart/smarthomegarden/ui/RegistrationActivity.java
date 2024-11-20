package ca.smartsprout.it.smart.smarthomegarden.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.firestore.FirebaseFirestore;

import ca.smartsprout.it.smart.smarthomegarden.MainActivity;
import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.ui.GoogleSignin.GoogleSignInHelper;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.AuthViewModel;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;

import ca.smartsprout.it.smart.smarthomegarden.data.model.User;

public class RegistrationActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput, passwordInput2, nameInput, phoneInput;
    private Button registerButton, Signingoogle;
    private AuthViewModel authViewModel;
    private GoogleSignInHelper googleSignInHelper;
    private FirebaseFirestore db;

    // Password pattern for validation: minimum 6 chars, at least one uppercase, one digit, and one special char
    private final String passwordPattern = "^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>])[A-Za-z\\d!@#$%^&*(),.?\":{}|<>]{6,}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration); // Use the appropriate layout file
        db = FirebaseFirestore.getInstance();
        emailInput = findViewById(R.id.editTextEmail1);
        passwordInput = findViewById(R.id.editTextPassword1);
        registerButton = findViewById(R.id.button2);
        Signingoogle = findViewById(R.id.googleButton);
        nameInput = findViewById(R.id.editTextName1);
        passwordInput2 = findViewById(R.id.editTextPassword2);
        phoneInput = findViewById(R.id.editTextPhone1);

        // Set filters for inputs
        passwordInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        passwordInput2.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        phoneInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        phoneInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        // Initialize ViewModel
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        // Email validation
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

        // Set click listener for Google Sign-In button
        googleSignInHelper = new GoogleSignInHelper(this);
        Signingoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("RegistrationActivity", "Google Sign-In button clicked");

                if (!googleSignInHelper.isSignedIn()) {
                    googleSignInHelper.signIn(result -> {
                        Log.d("RegistrationActivity", "Sign-In result: " + result);
                        if (result) {
                            runOnUiThread(() -> {
                                Toast.makeText(RegistrationActivity.this, "Sign-In Successful!", Toast.LENGTH_SHORT).show();
                                goToHomeScreen();
                            });
                        } else {
                            runOnUiThread(() -> {
                                Toast.makeText(RegistrationActivity.this, "Sign-In Failed. Please try again.", Toast.LENGTH_SHORT).show();
                            });
                        }
                        return null;
                    });
                } else {
                    Toast.makeText(RegistrationActivity.this, "Already Signed In!", Toast.LENGTH_SHORT).show();
                    goToHomeScreen();
                }
            }
        });

        // Set click listener for register button
        registerButton.setOnClickListener(v -> registerUser());

        // Password validation during typing
        passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String password = s.toString();
                if (!password.matches(passwordPattern)) {
                    passwordInput.setError("Password must be at least 6 characters, include an uppercase letter, a digit, and a special character.");
                } else {
                    passwordInput.setError(null); // Clear error if valid
                }
            }
        });

        // Confirm password validation
        passwordInput2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String confirmPassword = s.toString();
                String password = passwordInput.getText().toString();
                if (!confirmPassword.equals(password)) {
                    passwordInput2.setError("Passwords do not match.");
                } else {
                    passwordInput2.setError(null); // Clear error if match
                }
            }
        });
    }

    private void goToHomeScreen() {
        Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void registerUser() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String confirmPassword = passwordInput2.getText().toString().trim();
        String name = nameInput.getText().toString().trim();
        String phone = phoneInput.getText().toString().trim();

        // Check if email is valid
        if (!authViewModel.isValidEmail(email)) {
            emailInput.setError(getString(R.string.invalidemail));
            return;
        }

        // Check if password is valid
        if (!password.matches(passwordPattern)) {
            passwordInput.setError("Password must be at least 6 characters, include an uppercase letter, a digit, and a special character.");
            return;
        }

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            passwordInput2.setError("Passwords do not match.");
            return;
        }
        if (phone.isEmpty() || !phone.matches("^[0-9]{10}$")) { // Check if phone is valid
            phoneInput.setError(getString(R.string.invalidphone));
            return;
        }


        // Register the user
        authViewModel.registerUser(email, password).observe(this, authResult -> {
            if (authResult != null && authResult.getUser() != null) {
                String uid = authResult.getUser().getUid();
                User user = new User(name, phone, email, password, confirmPassword);
                authViewModel.saveUserDataToFirestore(uid, user).observe(this, success -> {
                    if (success) {
                        Toast.makeText(this, getString(R.string.registration), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this, getString(R.string.faileddata), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, getString(R.string.registration_failed), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
