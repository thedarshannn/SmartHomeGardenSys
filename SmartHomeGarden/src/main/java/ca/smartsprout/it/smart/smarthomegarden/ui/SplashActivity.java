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
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import ca.smartsprout.it.smart.smarthomegarden.MainActivity;
import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.utils.Constants;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.AuthViewModel;


@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    /**
     * Displays a splash screen and navigates to MainActivity after a delay.
     */

    private Handler handler = new Handler();
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        // Observe the login status to decide where to go
        authViewModel.getLoginStatus().observe(this, loggedIn -> {
            handler.postDelayed(() -> {
                if (isRememberMeEnabled() && loggedIn) {
                    // User wants to be remembered and is logged in
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    // User is not logged in or does not want to be remembered
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                finish();
            }, 3000); // 3 seconds delay before transitioning
        });

        // Check the login status as soon as the splash screen loads
        authViewModel.checkLoggedInStatus();
    }


    private boolean isRememberMeEnabled() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFS_USER_SESSION, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("remember_me", false);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}