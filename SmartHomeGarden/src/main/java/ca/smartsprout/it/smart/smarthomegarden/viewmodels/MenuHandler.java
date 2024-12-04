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


    public MenuHandler(Context context) {
        this.context = context;

    }

    public boolean handleMenuItem(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(context, SettingsActivity.class);
            context.startActivity(intent);
            return true;

        } else if (id == R.id.action_toggle_temp) {
            // Handle temperature unit toggle
            weatherViewModel.toggleTemperatureUnit();
            return true;
        } else if (id == R.id.faq) {
            Intent intent = new Intent(context, FAQActivity.class);
            context.startActivity(intent);

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
