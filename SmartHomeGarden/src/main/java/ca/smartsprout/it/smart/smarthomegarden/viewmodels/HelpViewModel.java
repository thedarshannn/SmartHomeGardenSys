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
import androidx.lifecycle.ViewModel;

import ca.smartsprout.it.smart.smarthomegarden.data.repository.HelpRepository;

public class HelpViewModel extends ViewModel {
    private final HelpRepository helpRepository;

    public HelpViewModel(Context context) {
        helpRepository = new HelpRepository(context);
    }

    public void sendHelpRequest(String description) {
        helpRepository.sendHelpRequest(description);
    }
}




