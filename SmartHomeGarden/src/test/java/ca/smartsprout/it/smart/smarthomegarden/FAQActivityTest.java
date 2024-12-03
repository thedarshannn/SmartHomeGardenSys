package ca.smartsprout.it.smart.smarthomegarden;

import android.view.View;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.recyclerview.widget.RecyclerView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.ui.FAQActivity;

import static org.junit.Assert.assertNotNull;

@Config(sdk = {33})
@RunWith(AndroidJUnit4.class)
public class FAQActivityTest {

    @Rule
    public ActivityScenarioRule<FAQActivity> activityScenarioRule = new ActivityScenarioRule<>(FAQActivity.class);

    @Test
    public void testRecyclerViewExists() {
        activityScenarioRule.getScenario().onActivity(activity -> {
            View recyclerView = activity.findViewById(R.id.faqRecyclerView);

            // Verify RecyclerView is not null
            assertNotNull("RecyclerView is null", recyclerView);

            // Verify the RecyclerView is of the correct type
            assert recyclerView instanceof RecyclerView;
        });
    }
}