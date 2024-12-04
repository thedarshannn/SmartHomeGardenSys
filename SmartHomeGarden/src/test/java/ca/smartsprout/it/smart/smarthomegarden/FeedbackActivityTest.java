package ca.smartsprout.it.smart.smarthomegarden;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Handler;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.test.core.app.ApplicationProvider;

import com.google.firebase.FirebaseApp;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import ca.smartsprout.it.smart.smarthomegarden.ui.FeedbackActivity;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 30) // Specify the SDK version to use
public class FeedbackActivityTest {

    @Mock
    private EditText descriptionEditText;
    @Mock
    private RatingBar ratingBar;
    @Mock
    private TextView nameTextView;
    @Mock
    private TextView emailTextView;
    @Mock
    private TextView phoneTextView;
    @Mock
    private TextView timerTextView;
    @Mock
    private SharedPreferences sharedPreferences;
    @Mock
    private SharedPreferences.Editor editor;
    @Mock
    private Handler handler;

    private FeedbackActivity feedbackActivity;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        Application application = ApplicationProvider.getApplicationContext();
        FirebaseApp.initializeApp(application);
        feedbackActivity = Robolectric.buildActivity(FeedbackActivity.class).create().get();
        feedbackActivity.descriptionEditText = descriptionEditText;
        feedbackActivity.ratingBar = ratingBar;
        feedbackActivity.nameTextView = nameTextView;
        feedbackActivity.emailTextView = emailTextView;
        feedbackActivity.phoneTextView = phoneTextView;
        feedbackActivity.timerTextView = timerTextView;
        feedbackActivity.sharedPreferences = sharedPreferences;
        feedbackActivity.handler = handler;

        when(sharedPreferences.edit()).thenReturn(editor);
    }


    @Test
    public void testClearInputFields() {
        // Arrange
        doNothing().when(descriptionEditText).setText("");
        doNothing().when(ratingBar).setRating(0);

        // Act
        feedbackActivity.clearInputFields();

        // Assert
        verify(descriptionEditText).setText("");
        verify(ratingBar).setRating(0);
    }

    @Test
    public void testSetFieldsEnabled() {
        // Arrange
        boolean enabled = true;

        // Act
        feedbackActivity.setFieldsEnabled(enabled);

        // Assert
        verify(nameTextView).setEnabled(enabled);
        verify(emailTextView).setEnabled(enabled);
        verify(phoneTextView).setEnabled(enabled);
        verify(ratingBar).setEnabled(enabled);
        verify(descriptionEditText).setEnabled(enabled);
    }

}
