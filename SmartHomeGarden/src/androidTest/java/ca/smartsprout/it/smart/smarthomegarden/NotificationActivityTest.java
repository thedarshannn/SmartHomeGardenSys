package ca.smartsprout.it.smart.smarthomegarden;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ca.smartsprout.it.smart.smarthomegarden.ui.NotificationActivity;

@RunWith(AndroidJUnit4.class)
public class NotificationActivityTest {

    @Rule
    public ActivityScenarioRule<NotificationActivity> activityScenarioRule =
            new ActivityScenarioRule<>(NotificationActivity.class);

    @Test
    public void testRecyclerViewIsDisplayed() {
        // Check if the RecyclerView for notifications is displayed
        Espresso.onView(ViewMatchers.withId(R.id.recycler_view_notifications))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
}

