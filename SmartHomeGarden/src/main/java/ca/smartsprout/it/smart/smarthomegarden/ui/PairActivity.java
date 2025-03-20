package ca.smartsprout.it.smart.smarthomegarden.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ca.smartsprout.it.smart.smarthomegarden.MainActivity;
import ca.smartsprout.it.smart.smarthomegarden.R;

public class PairActivity extends AppCompatActivity {

    private TextInputEditText etPiName;
    private MaterialButton btnPair;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference databaseRef;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pair);

        etPiName = findViewById(R.id.et_pi_name);
        btnPair = findViewById(R.id.btn_pair);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        databaseRef = FirebaseDatabase.getInstance().getReference();
        executorService = Executors.newSingleThreadExecutor(); // Thread pool for background tasks

        btnPair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pairRaspberryPi();
            }
        });
    }

    private void pairRaspberryPi() {
        if (user == null) {
            Toast.makeText(this, R.string.user_not_authenticated, Toast.LENGTH_SHORT).show();
            return;
        }

        String piName = etPiName.getText().toString().trim();
        if (piName.isEmpty()) {
            etPiName.setError(getString(R.string.enter_a_valid_pi_name));
            return;
        }

        // Execute Firebase operations in a separate thread
        executorService.execute(() -> {
            user.getIdToken(true).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    String idToken = task.getResult().getToken();

                    HashMap<String, String> data = new HashMap<>();
                    data.put("userId", user.getUid());
                    data.put("idToken", idToken);

                    DatabaseReference ref = databaseRef.child("devices").child(piName);

                    ref.setValue(data)
                            .addOnSuccessListener(aVoid -> {
                                savePiNameLocally(piName);
                                runOnUiThread(() -> {
                                    Toast.makeText(PairActivity.this, "Paired successfully!", Toast.LENGTH_SHORT).show();
                                    Log.d("Pairing", "Pi name stored in Firebase!");

                                    // Redirect to main activity
                                    startActivity(new Intent(PairActivity.this, MainActivity.class));
                                    finish();
                                });
                            })
                            .addOnFailureListener(e -> runOnUiThread(() -> {
                                Log.e("Pairing", "❌ Failed to pair device", e);
                                Toast.makeText(PairActivity.this, "Failed to pair device. Try again.", Toast.LENGTH_SHORT).show();
                            }));
                } else {
                    runOnUiThread(() -> {
                        Log.e("Pairing", "❌ Failed to get idToken", task.getException());
                        Toast.makeText(PairActivity.this, "Authentication failed!", Toast.LENGTH_SHORT).show();
                    });
                }
            });
        });
    }

    private void savePiNameLocally(String piName) {
        SharedPreferences prefs = getSharedPreferences("SmartSproutPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("Pi_Name", piName);
        editor.apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown(); // Shut down thread pool to release resources
    }
}