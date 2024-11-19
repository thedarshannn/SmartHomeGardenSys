package ca.smartsprout.it.smart.smarthomegarden.data.repository;

import android.content.Context;
import android.content.Intent;

import ca.smartsprout.it.smart.smarthomegarden.R;


public class HelpRepository {
    private final Context context;

    public HelpRepository(Context context) {
        this.context = context;
    }
    public void sendHelpRequest(String description) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{context.getString(R.string.email_address)});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.email_subject));
        emailIntent.putExtra(Intent.EXTRA_TEXT, description);
        context.startActivity(Intent.createChooser(emailIntent, context.getString(R.string.email_chooser_title)));
    }
}

