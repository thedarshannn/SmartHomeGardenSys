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

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.Visibility;

@RunWith(AndroidJUnit4.class)
public class FeedbackActivityTest {

    @Rule
    public ActivityScenarioRule<FeedbackActivity> activityScenarioRule =
            new ActivityScenarioRule<>(FeedbackActivity.class);

    @Test
    public void testSubmitButtonIsDisplayedAndClickable() {
        // Check if the submit button is displayed
        Espresso.onView(ViewMatchers.withId(R.id.feedbackbutton))
                .check(ViewAssertions.matches(isDisplayed()));

        // Check if the submit button is clickable
        Espresso.onView(ViewMatchers.withId(R.id.feedbackbutton))
                .check(ViewAssertions.matches(ViewMatchers.isClickable()));
    }

    @Test
    public void testProgressBarIsHiddenInitially() {
        onView(withId(R.id.progressBar))
                .check(matches(withEffectiveVisibility(Visibility.GONE)));
    }

    @Test
    public void testRatingBarIsDisplayed() {
        onView(withId(R.id.ratingBar))
                .check(matches(isDisplayed()));
    }
    @Test
    public void testNameTextViewIsDisplayed() {
        onView(withId(R.id.nameTextView))
                .check(matches(isDisplayed()));
    }
    @Test
    public void testEmailTextViewIsDisplayed() {
        onView(withId(R.id.EmailfetchtextView2))
                .check(matches(isDisplayed()));
    }
}
