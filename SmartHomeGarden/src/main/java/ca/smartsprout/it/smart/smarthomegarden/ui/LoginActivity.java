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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import ca.smartsprout.it.smart.smarthomegarden.ui.GoogleSignin.GoogleSignInHelper;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.AuthResult;

import ca.smartsprout.it.smart.smarthomegarden.MainActivity;
import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.AuthViewModel;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.ViewModelProvider;



public class LoginActivity extends AppCompatActivity {
    private EditText emailInput, passwordInput;
    private Button loginButton,googlesignin;
    private AuthViewModel authViewModel;
private TextView registerswitch;
    private GoogleSignInHelper googleSignInHelpers;
    private CheckBox rememberMeCheckbox;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);  // Set your activity's layout

        // Initialize UI elements
        emailInput = findViewById(R.id.editTextEmail);
        passwordInput = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.button);
registerswitch=findViewById(R.id.registerswitch);
        rememberMeCheckbox = findViewById(R.id.rememberMeCheckbox);
        googlesignin=findViewById(R.id.googlesignin);
        googleSignInHelpers = new GoogleSignInHelper(this);
        // Initialize ViewModel
        passwordInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        emailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}



            // Check for saved credentials


            @Override
            public void afterTextChanged(Editable s) {
                if (!Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                    emailInput.setError(getString(R.string.invalidemail));
                }
            }
        });
        sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        registerswitch.setOnClickListener(v -> {
            // Call the method to load RegistrationFragment

            Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(intent);
        });
        loadLoginDetails();

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

        // Set click listener for login button
        loginButton.setOnClickListener(v -> loginUser());


    }

    private void goToHomeScreen() {
        // Example: Navigate to HomeActivity after successful sign-in
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void loadLoginDetails() {
        boolean rememberMe = sharedPreferences.getBoolean("rememberMe", false);
        if (rememberMe) {
            String savedEmail = sharedPreferences.getString("email", "");
            String savedPassword = sharedPreferences.getString("password", "");
            emailInput.setText(savedEmail);
            passwordInput.setText(savedPassword);
            rememberMeCheckbox.setChecked(true);
        }
    }

    private void saveLoginDetails() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (rememberMeCheckbox.isChecked()) {
            editor.putBoolean("rememberMe", true);
            editor.putString("email", emailInput.getText().toString().trim());
            editor.putString("password", passwordInput.getText().toString().trim());
        } else {
            editor.clear();
        }
        editor.apply();
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
            saveLoginDetails();
            // Observe the login result from ViewModel
            authViewModel.loginUser(email, password).observe(this, this::handleLoginResult);
        }
    }

    private void handleLoginResult(@Nullable AuthResult authResult) {
        if (authResult != null) {

            Toast.makeText(this, getString(R.string.login), Toast.LENGTH_SHORT).show();
            // Navigate to the home screen or another activity here
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
        }
    }



}