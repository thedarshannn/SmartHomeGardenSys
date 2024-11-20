/**
 * Smart Sprout
 * Members:
 * 1. Aditi Patel, n01525570, CENG322-RCB
 * 2. Birava Prajapati, n01579924, CENG322-RCA
 * 3. Darshankumar Prajapati, n01584247, CENG322-RCB
 * 4. Zeel Patel, n01526282, CENG322-RCB
 */

package ca.smartsprout.it.smart.smarthomegarden.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import ca.smartsprout.it.smart.smarthomegarden.MainActivity;
import ca.smartsprout.it.smart.smarthomegarden.R;


@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    /**
     * Displays a splash screen and navigates to MainActivity after a delay.
     */

    private Handler handler = new Handler();
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        sharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE);
        boolean rememberMe = sharedPreferences.getBoolean("rememberMe", false);
        if (rememberMe) {
            // If "Remember Me" is true, skip the login and go directly to MainActivity with HomeFragment
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.putExtra("openHomeFragment", true); // Add extra to open HomeFragment
                    startActivity(intent);
                    finish();
                }
            }, 3000); // 3-second delay for splash screen
        } else {
            // If "Remember Me" is false, proceed to the login screen
            runnable = new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            };
            handler.postDelayed(runnable, 3000); // 3-second delay for splash screen
        }
    }





    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
}