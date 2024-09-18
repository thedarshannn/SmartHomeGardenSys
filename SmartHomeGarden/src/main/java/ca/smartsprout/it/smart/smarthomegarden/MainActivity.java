package ca.smartsprout.it.smart.smarthomegarden;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Alert dialog box which shows up when user click on back button.
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // Show exit confirmation dialog
            showExitConfirmationDialog();
            return true;  // Indicate that the event has been handled
        }
        return super.onKeyUp(keyCode, event);
    }

    /**
     * Displays a confirmation dialog asking the user if they want to exit the application.
     *
     * <p>The dialog shows a title, an icon, and a message prompting the user to confirm their choice.
     * The dialog has two buttons: "Yes" and "No". If the user selects "Yes", the application
     * will close using {@link #finish()}. If the user selects "No", the dialog will be dismissed.</p>
     *
     * <p>The dialog is non-cancelable, meaning the user must explicitly choose either "Yes" or "No".</p>
     */
    public void showExitConfirmationDialog() {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit");
        builder.setIcon(R.drawable.alert);
        builder.setMessage("Do you want to exit the application?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.create().show();
    }
}