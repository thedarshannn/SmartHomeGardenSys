package ca.smartsprout.it.smart.smarthomegarden.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.AuthResult;

import ca.smartsprout.it.smart.smarthomegarden.MainActivity;
import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.AuthViewModel;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.ViewModelProvider;



public class Login extends AppCompatActivity {
    private EditText emailInput, passwordInput;
    private Button loginButton;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);  // Set your activity's layout

        // Initialize UI elements
        emailInput = findViewById(R.id.editTextEmail);
        passwordInput = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.button);

        // Initialize ViewModel
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

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
            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
            // Navigate to the home screen or another activity here
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Login failed.", Toast.LENGTH_SHORT).show();
        }
    }


}