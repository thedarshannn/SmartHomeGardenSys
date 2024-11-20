/**
 * Smart Sprout
 * Members:
 * 1. Aditi Patel, n01525570, CENG322-RCB
 * 2. Birava Prajapati, n01579924, CENG322-RCA
 * 3. Darshankumar Prajapati, n01584247, CENG322-RCB
 * 4. Zeel Patel, n01526282, CENG322-RCB
 */
package ca.smartsprout.it.smart.smarthomegarden;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.Task;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ca.smartsprout.it.smart.smarthomegarden.viewmodels.PasswordViewModel;

public class PasswordViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private FirebaseAuth mockFirebaseAuth;

    @Mock
    private FirebaseUser mockFirebaseUser;

    @Mock
    private Observer<Boolean> observer;

    private PasswordViewModel passwordViewModel;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        passwordViewModel = new PasswordViewModel(mockFirebaseAuth);
        passwordViewModel.firebaseAuth = mockFirebaseAuth;
    }

    @Test
    public void testValidateCurrentPassword_Success() {
        // Mock FirebaseAuth and FirebaseUser
        when(mockFirebaseAuth.getCurrentUser()).thenReturn(mockFirebaseUser);
        when(mockFirebaseUser.getEmail()).thenReturn("test@example.com");

        // Mock a successful Task for authentication
        Task successTask = mock(Task.class);
        when(successTask.isSuccessful()).thenReturn(true);
        when(successTask.addOnCompleteListener(any())).thenAnswer(invocation -> {
            OnCompleteListener<Void> listener = invocation.getArgument(0);
            listener.onComplete(successTask);
            return successTask;
        });
        when(successTask.addOnFailureListener(any())).thenReturn(successTask); // Handle chaining

        // Configure authenticate to return the mock Task
        when(mockFirebaseUser.reauthenticate(any(AuthCredential.class))).thenReturn(successTask);

        // Attach an observer to the LiveData
        passwordViewModel.getCurrentPasswordValidation().observeForever(observer);

        // Call the method under test
        passwordViewModel.validateCurrentPassword("correctPassword");

        // Verify the observer is notified with "true"
        assertEquals(passwordViewModel.getCurrentPasswordValidation().getValue(), true);
    }

    @Test
    public void testValidateCurrentPassword_NoUser() {
        when(mockFirebaseAuth.getCurrentUser()).thenReturn(null);

        passwordViewModel.getCurrentPasswordValidation().observeForever(observer);
        passwordViewModel.validateCurrentPassword("anyPassword");

        assertNotEquals(passwordViewModel.getCurrentPasswordValidation().getValue(), true);
    }

    @Test
    public void testCheckPasswordsMatch_Match() {
        passwordViewModel.getPasswordsMatch().observeForever(observer);
        passwordViewModel.checkPasswordsMatch("password123", "password123");

        assertEquals(passwordViewModel.getPasswordsMatch().getValue(), true);
    }

    @Test
    public void testCheckPasswordsMatch_NoMatch() {
        passwordViewModel.getPasswordsMatch().observeForever(observer);
        passwordViewModel.checkPasswordsMatch("password123", "password456");

        assertFalse(passwordViewModel.getPasswordsMatch().getValue());
    }

    @Test
    public void testUpdatePassword_NoUser() {
        passwordViewModel.getUpdateStatus().observeForever(observer);
        assertNull(passwordViewModel.getUpdateStatus().getValue());
    }

    @Test
    public void testHandleEmptyPassword() {
        passwordViewModel.getCurrentPasswordValidation().observeForever(observer);
        passwordViewModel.handleEmptyPassword();

        // Verify that the LiveData is not null and its value is false
        assertNotNull(passwordViewModel.getCurrentPasswordValidation().getValue());
        assertFalse(passwordViewModel.getCurrentPasswordValidation().getValue());
    }

    @Test
    public void testValidateCurrentPassword_EmptyPassword() {
        passwordViewModel.getCurrentPasswordValidation().observeForever(observer);
        passwordViewModel.validateCurrentPassword("");

        // Verify that the LiveData is not null and its value is false
        assertNotNull(passwordViewModel.getCurrentPasswordValidation().getValue());
        assertFalse(passwordViewModel.getCurrentPasswordValidation().getValue());
    }
}
