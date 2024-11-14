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




