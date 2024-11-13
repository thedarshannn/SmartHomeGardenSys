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

import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import android.content.Intent;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.AuthResult;


import ca.smartsprout.it.smart.smarthomegarden.MainActivity;
import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.ui.GoogleSignin.GoogleSignInHelper;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.AuthViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;




import ca.smartsprout.it.smart.smarthomegarden.data.model.User;



public class RegistrationActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput,passwordInput2,nameInput,phoneInput;
    private Button registerButton,Signingoogle;
    private AuthViewModel authViewModel;
    private GoogleSignInHelper googleSignInHelper;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration); // Use the appropriate layout file
        db = FirebaseFirestore.getInstance();
        emailInput = findViewById(R.id.editTextEmail1);
        passwordInput = findViewById(R.id.editTextPassword1);
        registerButton = findViewById(R.id.button2);
Signingoogle=findViewById(R.id.googleButton);
        nameInput = findViewById(R.id.editTextName1);
        passwordInput2=findViewById(R.id.editTextPassword2);
        phoneInput = findViewById(R.id.editTextPhone1);
        passwordInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        passwordInput2.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        phoneInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        // Initialize ViewModel
       // saveUserProfile();
        // Email validation
        googleSignInHelper = new GoogleSignInHelper(this);
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
        int status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        Log.d("GooglePlayServices", "Google Play Services status: " + status);
        Signingoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("RegistrationActivity", "Google Sign-In button clicked");

                if (!googleSignInHelper.isSignedIn()) {
                    // Trigger Google Sign-In
                    googleSignInHelper.signIn(result -> {
                        Log.d("RegistrationActivity", "Sign-In result: " + result); // Log the result
                        if (result) {
                            // Handle successful sign-in
                            runOnUiThread(() -> {
                                Toast.makeText(RegistrationActivity.this, "Sign-In Successful!", Toast.LENGTH_SHORT).show();
                                // Proceed to the next activity or update the UI
                                goToHomeScreen();
                            });
                        } else {
                            // Handle sign-in failure
                            runOnUiThread(() -> {
                                Toast.makeText(RegistrationActivity.this, "Sign-In Failed. Please try again.", Toast.LENGTH_SHORT).show();
                            });
                        }
                        return null;
                    });
                } else {
                    // Already signed in, proceed to the next activity or handle accordingly
                    Toast.makeText(RegistrationActivity.this, "Already Signed In!", Toast.LENGTH_SHORT).show();
                    goToHomeScreen();
                }
            }
        });

        // Set click listener for register button
        registerButton.setOnClickListener(v -> registerUser());

    }



    private void goToHomeScreen() {
        // Example: Navigate to HomeActivity after successful sign-in
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

        if (!authViewModel.isValidEmail(email)) {
            emailInput.setError(getString(R.string.invalidemail));
            return;
        }

        if (!authViewModel.isValidPassword(password)) {
            passwordInput.setError(getString(R.string.exceed));
            return;
        }

        if (!password.equals(confirmPassword)) {
            passwordInput2.setError(getString(R.string.match));
            return;
        }

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





