package ca.smartsprout.it.smart.smarthomegarden;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.text.Editable;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.core.app.ApplicationProvider;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import ca.smartsprout.it.smart.smarthomegarden.ui.LoginActivity;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.AuthViewModel;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 30) // Specify the SDK version to use
public class LoginActivityTest {

    @Mock
    private EditText emailInput;
    @Mock
    private EditText passwordInput;
    @Mock
    private Button loginButton;
    @Mock
    private Button googlesignin;
    @Mock
    private TextView registerswitch;
    @Mock
    private CheckBox rememberMeCheckbox;
    @Mock
    private SharedPreferences sharedPreferences;
    @Mock
    private SharedPreferences.Editor editor;
    @Mock
    private FirebaseAuth mAuth;
    @Mock
    private FirebaseUser currentUser;
    @Mock
    private AuthViewModel authViewModel;

    private LoginActivity loginActivity;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        Application application = ApplicationProvider.getApplicationContext();
        FirebaseApp.initializeApp(application);
        loginActivity = Robolectric.buildActivity(LoginActivity.class).create().get();
        loginActivity.emailInput = emailInput;
        loginActivity.passwordInput = passwordInput;
        loginActivity.loginButton = loginButton;
        loginActivity.registerswitch = registerswitch;
        loginActivity.rememberMeCheckbox = rememberMeCheckbox;
        loginActivity.googlesignin = googlesignin;
        loginActivity.sharedPreferences = sharedPreferences;
        loginActivity.mAuth = mAuth;
        loginActivity.currentUser = currentUser;
        loginActivity.authViewModel = authViewModel;

        when(sharedPreferences.edit()).thenReturn(editor);
    }

    @Test
    public void testLoadLoginDetails_rememberMeChecked() {
        // Arrange
        when(sharedPreferences.getBoolean("rememberMe", false)).thenReturn(true);
        when(sharedPreferences.getString("email", "")).thenReturn("test@example.com");
        when(sharedPreferences.getString("password", "")).thenReturn("password123");

        // Act
        loginActivity.loadLoginDetails();

        // Assert
        verify(emailInput).setText("test@example.com");
        verify(passwordInput).setText("password123");
        verify(rememberMeCheckbox).setChecked(true);
    }

    @Test
    public void testSaveLoginDetails_rememberMeChecked() {
        // Arrange
        when(rememberMeCheckbox.isChecked()).thenReturn(true);
        when(emailInput.getText()).thenReturn(mock(Editable.class));
        when(passwordInput.getText()).thenReturn(mock(Editable.class));
        when(emailInput.getText().toString()).thenReturn("test@example.com");
        when(passwordInput.getText().toString()).thenReturn("password123");

        // Act
        loginActivity.saveLoginDetails();

        // Assert
        verify(editor).putBoolean("rememberMe", true);
        verify(editor).putString("email", "test@example.com");
        verify(editor).putString("password", "password123");
        verify(editor).apply();
    }

    @Test
    public void testSaveLoginDetails_rememberMeUnchecked() {
        // Arrange
        when(rememberMeCheckbox.isChecked()).thenReturn(false);

        // Act
        loginActivity.saveLoginDetails();

        // Assert
        verify(editor).clear();
        verify(editor).apply();
    }
}
