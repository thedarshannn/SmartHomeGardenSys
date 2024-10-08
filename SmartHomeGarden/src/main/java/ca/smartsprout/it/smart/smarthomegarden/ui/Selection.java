package ca.smartsprout.it.smart.smarthomegarden.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import ca.smartsprout.it.smart.smarthomegarden.R;

public class Selection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_selection);

        Button registerButton = findViewById(R.id.button3);  // Register Button
        TextView loginButton = findViewById(R.id.textView);  // Login TextView

        // Set click listener for Register Button
        registerButton.setOnClickListener(v -> {
            // Call the method to load RegistrationFragment

            Intent intent = new Intent(Selection.this, Registration.class);
            startActivity(intent);
        });

        // Set click listener for Login TextView
        loginButton.setOnClickListener(v -> {
              // Call the method to load LoginFragment
            Intent intent = new Intent(Selection.this, Login.class);
            startActivity(intent);

        });



    }
}