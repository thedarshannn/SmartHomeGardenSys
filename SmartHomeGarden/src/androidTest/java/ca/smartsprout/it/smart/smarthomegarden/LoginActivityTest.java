package ca.smartsprout.it.smart.smarthomegarden;
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
        Espresso.onView(ViewMatchers.withId(R.id.editTextEmail))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Check if the password input field is displayed
        Espresso.onView(ViewMatchers.withId(R.id.editTextPassword))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testRegisterSwitchIsClickable() {
        // Check if the register switch TextView is clickable
        Espresso.onView(ViewMatchers.withId(R.id.registerswitch))
                .check(ViewAssertions.matches(ViewMatchers.isClickable()));
    }
}
