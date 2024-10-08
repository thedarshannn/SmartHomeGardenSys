package ca.smartsprout.it.smart.smarthomegarden.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ca.smartsprout.it.smart.smarthomegarden.R;

public class SelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_selection);

        Button registerButton = findViewById(R.id.button3);  // Register Button
        TextView loginButton = findViewById(R.id.textView);  // LoginActivity TextView

        // Set click listener for Register Button
        registerButton.setOnClickListener(v -> {
            // Call the method to load RegistrationFragment

            Intent intent = new Intent(SelectionActivity.this, RegistrationActivity.class);
            startActivity(intent);
        });

        // Set click listener for LoginActivity TextView
        loginButton.setOnClickListener(v -> {
              // Call the method to load LoginFragment
            Intent intent = new Intent(SelectionActivity.this, LoginActivity.class);
            startActivity(intent);

        });



    }
}