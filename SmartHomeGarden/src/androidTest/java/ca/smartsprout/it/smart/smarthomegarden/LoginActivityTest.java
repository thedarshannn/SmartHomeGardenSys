package ca.smartsprout.it.smart.smarthomegarden;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ca.smartsprout.it.smart.smarthomegarden.ui.LoginActivity;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> activityScenarioRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    @Test
    public void testEmailAndPasswordFieldsAreDisplayed() {
        // Check if the email input field is displayed
        onView(withId(R.id.editTextEmail))
                .check(matches(isDisplayed()));

        // Check if the password input field is displayed
        onView(withId(R.id.editTextPassword))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testRegisterSwitchIsClickable() {
        // Check if the register switch TextView is clickable
        onView(withId(R.id.registerswitch))
                .check(matches(ViewMatchers.isClickable()));
    }
    @Test
    public void testLoginButtonIsDisplayed() {
        onView(withId(R.id.button))
                .check(matches(isDisplayed()));
    }
    @Test
    public void testGoogleSignInButtonIsDisplayed() {
        onView(withId(R.id.googlesignin))
                .check(matches(isDisplayed()));
    }
    @Test
    public void testRememberMeCheckboxIsDisplayed() {
        onView(withId(R.id.rememberMeCheckbox))
                .check(matches(isDisplayed()));
    }
    @Test
    public void testRegisterSwitchIsDisplayed() {
        onView(withId(R.id.registerswitch))
                .check(matches(isDisplayed()));
    }
}
