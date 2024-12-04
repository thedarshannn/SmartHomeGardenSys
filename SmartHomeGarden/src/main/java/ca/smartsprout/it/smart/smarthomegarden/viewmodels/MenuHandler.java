/**
 * Smart Sprout
 * Members:
 * 1. Aditi Patel, n01525570, CENG322-RCB
 * 2. Birava Prajapati, n01579924, CENG322-RCA
 * 3. Darshankumar Prajapati, n01574247, CENG322-RCB
 * 4. Zeel Patel, n01526282, CENG322-RCB
 */

package ca.smartsprout.it.smart.smarthomegarden.viewmodels;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import androidx.fragment.app.FragmentActivity;

import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.ui.FAQActivity;
import ca.smartsprout.it.smart.smarthomegarden.ui.NotificationActivity;
import ca.smartsprout.it.smart.smarthomegarden.ui.SettingsActivity;
import ca.smartsprout.it.smart.smarthomegarden.ui.fragments.HelpBottomSheetFragment;

public class MenuHandler {

    private final Context context;
    private final WeatherViewModel weatherViewModel = null;


    public MenuHandler(Context context) {
        this.context = context;
    }

    public boolean handleMenuItem(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(context, SettingsActivity.class);
            context.startActivity(intent);
            return true;
        } else if (id == R.id.faq) {
            Intent intent = new Intent(context, FAQActivity.class);
            context.startActivity(intent);
            return true;
        } else if (id == R.id.help) {
            HelpBottomSheetFragment helpBottomSheetFragment = new HelpBottomSheetFragment();
            helpBottomSheetFragment.show(((FragmentActivity) context).getSupportFragmentManager(), helpBottomSheetFragment.getTag());

            return true;
        } else if (id == R.id.action_notification) {
            Intent intent = new Intent(context, NotificationActivity.class);
            context.startActivity(intent);
            return true;
        } else {
            return false;
        }
    }
}
