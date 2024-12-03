package ca.smartsprout.it.smart.smarthomegarden;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ca.smartsprout.it.smart.smarthomegarden.ui.FeedbackActivity;

@RunWith(AndroidJUnit4.class)
public class FeedbackActivityTest {

    @Rule
    public ActivityScenarioRule<FeedbackActivity> activityScenarioRule =
            new ActivityScenarioRule<>(FeedbackActivity.class);

    @Test
    public void testSubmitButtonIsDisplayedAndClickable() {
        // Check if the submit button is displayed
        Espresso.onView(ViewMatchers.withId(R.id.feedbackbutton))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Check if the submit button is clickable
        Espresso.onView(ViewMatchers.withId(R.id.feedbackbutton))
                .check(ViewAssertions.matches(ViewMatchers.isClickable()));
    }
}
