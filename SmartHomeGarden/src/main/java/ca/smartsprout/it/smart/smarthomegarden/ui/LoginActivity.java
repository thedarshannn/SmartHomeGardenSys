package ca.smartsprout.it.smart.smarthomegarden.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ca.smartsprout.it.smart.smarthomegarden.MainActivity;
import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.data.model.Notification;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.AuthViewModel;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class LoginActivity extends AppCompatActivity {
    private EditText emailInput, passwordInput;
    private Button loginButton;
    private AuthViewModel authViewModel;
    private TextView registerswitch;
    private CheckBox rememberMeCheckbox;
    private SharedPreferences sharedPreferences;
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

        sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        databaseReference = FirebaseDatabase.getInstance().getReference("notifications");

        registerswitch.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(intent);
        });

        loadLoginDetails();

        // Set click listener for login button
        loginButton.setOnClickListener(v -> loginUser());
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
            authViewModel.loginUser(email, password).observe(this, this::handleLoginResult);
        }
    }

    private void handleLoginResult(@Nullable AuthResult authResult) {
        if (authResult != null) {
            Toast.makeText(this, getString(R.string.login), Toast.LENGTH_SHORT).show();
            showLoginNotification();
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
        Notification notification = new Notification(notificationId, "Login Successful", "Welcome back!", timestamp);

        // Save the notification to the database
        if (notificationId != null) {
            databaseReference.child(notificationId).setValue(notification);
        }
    }

}
