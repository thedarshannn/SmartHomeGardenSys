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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ca.smartsprout.it.smart.smarthomegarden.data.model.User;
import ca.smartsprout.it.smart.smarthomegarden.data.repository.FirebaseRepository;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.UserViewModel;


public class UserViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private FirebaseRepository mockFirebaseRepository;

    @Mock
    private FirebaseAuth mockFirebaseAuth;

    @Mock
    private FirebaseUser mockFirebaseUser;

    private UserViewModel userViewModel;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock FirebaseAuth and FirebaseUser
        when(mockFirebaseAuth.getCurrentUser()).thenReturn(mockFirebaseUser);
        when(mockFirebaseUser.getUid()).thenReturn("mockUserId");

        // Mock FirebaseRepository
        mockFirebaseRepository = mock(FirebaseRepository.class);

        // Inject mocked repository into UserViewModel
        userViewModel = new UserViewModel(mockFirebaseRepository);
    }

    @Test
    public void testGetUserName_Success() {
        // Mock the repository to return a LiveData object with a User
        MutableLiveData<User> userLiveData = new MutableLiveData<>();
        User mockUser = new User("Test User", "77777777", "testuser@example.com", "mockToken", "mockRefreshToken");
        userLiveData.setValue(mockUser);

        when(mockFirebaseRepository.fetchUserDetails()).thenReturn(userLiveData);

        // Observe and verify the userName LiveData
        userViewModel.getUserName().observeForever(name -> {
            assertNotNull(name);
            assertEquals("Test User", name);
        });
    }


    @Test
    public void testUpdateUserName() {
        // Call the updateUserName method
        userViewModel.updateUserName("Updated User");

        // Verify that the repository's updateUserName method was called
        verify(mockFirebaseRepository).updateUserName("Updated User");

        // Verify the userName LiveData was updated
        userViewModel.getUserName().observeForever(name -> {
            assertNotNull(name);
            assertTrue(name.equals("Updated User"));
        });
    }

}
