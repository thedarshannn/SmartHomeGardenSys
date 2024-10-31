/**
 * Smart Sprout
 * Members:
 * 1. Aditi Patel, n01525570, CENG322-RCB
 * 2. Birava Prajapati, n01579924, CENG322-RCA
 * 3. Darshankumar Prajapati, n01584247, CENG322-RCB
 * 4. Zeel Patel, n01526282, CENG322-RCB
 */
package ca.smartsprout.it.smart.smarthomegarden.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.AuthResult;

import ca.smartsprout.it.smart.smarthomegarden.MainActivity;
import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.AuthViewModel;

public class RegistrationActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private Button registerButton;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration); // Use the appropriate layout file

        // Initialize UI elements
        emailInput = findViewById(R.id.editTextEmail1);
        passwordInput = findViewById(R.id.editTextPassword1);
        registerButton = findViewById(R.id.button2);
        passwordInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        // Initialize ViewModel

        // Email validation
        emailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (!Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                    emailInput.setError("Invalid email format");
                }
            }
        });


        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        // Set click listener for register button
        registerButton.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email and Password are required", Toast.LENGTH_SHORT).show();
        } else if (password.length() > 10) {
            passwordInput.setError("Password cannot exceed 10 characters");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Enter a valid email");
        } else {
            //Toast.makeText(MainActivity.this, "Valid Email and Password", Toast.LENGTH_SHORT).show();

            // Register user and observe result
            authViewModel.registerUser(email, password).observe(this, this::handleRegistrationResult);
        }
    }

    private void handleRegistrationResult(AuthResult authResult) {
        if (authResult != null) {
            Toast.makeText(this, "@string/registration", Toast.LENGTH_SHORT).show();
            // Navigate to the home screen or another activity here
            Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
            startActivity(intent);

        }

        else {
            Toast.makeText(this, "@string/registration_failed", Toast.LENGTH_SHORT).show();
        }
    }
}