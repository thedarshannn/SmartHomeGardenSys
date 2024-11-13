package ca.smartsprout.it.smart.smarthomegarden.menu;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.MenuItem;

import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.ui.NotificationActivity;
import ca.smartsprout.it.smart.smarthomegarden.ui.SettingsActivity;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.WeatherViewModel;

public class MenuHandler {

    private final Context context;
    private final WeatherViewModel weatherViewModel;

    public MenuHandler(Context context, WeatherViewModel weatherViewModel) {
        this.context = context;
        this.weatherViewModel = weatherViewModel;
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
        } else if (id == R.id.help) {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse(context.getString(R.string.mailto_smartsprout_gmail_com)));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.help_request));
            context.startActivity(Intent.createChooser(emailIntent, context.getString(R.string.send_email)));
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
