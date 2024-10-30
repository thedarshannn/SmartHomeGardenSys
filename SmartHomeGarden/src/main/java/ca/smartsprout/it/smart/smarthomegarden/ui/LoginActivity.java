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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.AuthResult;

import ca.smartsprout.it.smart.smarthomegarden.MainActivity;
import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.AuthViewModel;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.ViewModelProvider;



public class LoginActivity extends AppCompatActivity {
    private EditText emailInput, passwordInput;
    private Button loginButton;
    private AuthViewModel authViewModel;
private TextView registerswitch;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);  // Set your activity's layout

        // Initialize UI elements
        emailInput = findViewById(R.id.editTextEmail);
        passwordInput = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.button);
registerswitch=findViewById(R.id.registerswitch);
        // Initialize ViewModel
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        registerswitch.setOnClickListener(v -> {
            // Call the method to load RegistrationFragment

            Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(intent);
        });

        // Set click listener for login button
        loginButton.setOnClickListener(v -> loginUser());


    }

    private void loginUser() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        // Observe the login result from ViewModel
        authViewModel.loginUser(email, password).observe(this, this::handleLoginResult);
    }

    private void handleLoginResult(@Nullable AuthResult authResult) {
        if (authResult != null) {
            Toast.makeText(this, "@string/login", Toast.LENGTH_SHORT).show();
            // Navigate to the home screen or another activity here
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "@string/login_failed", Toast.LENGTH_SHORT).show();
        }
    }


}