package ca.smartsprout.it.smart.smarthomegarden.ui.fragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.HelpViewModel;

public class HelpBottomSheetFragment extends BottomSheetDialogFragment {

    private EditText editTextDescription;
    private Button buttonSend;
    private HelpViewModel helpViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help_bottom_sheet, container, false);
        editTextDescription = view.findViewById(R.id.editTextDescription);
        buttonSend = view.findViewById(R.id.buttonSend);
        helpViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new HelpViewModel(requireContext());
            }
        }).get(HelpViewModel.class);

        buttonSend.setOnClickListener(v -> {
            String description = editTextDescription.getText().toString().trim();
            if (!description.isEmpty()) {
                helpViewModel.sendHelpRequest(description);
                dismiss();
            }
        });

        return view;
    }
}
