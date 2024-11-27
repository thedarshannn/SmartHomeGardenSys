package ca.smartsprout.it.smart.smarthomegarden.ui.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.RadioGroup;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import java.util.Calendar;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.data.model.PlantTask;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.PlantTaskViewModel;

public class CustomBottomSheetFragment extends BottomSheetDialogFragment {

    private PlantTaskViewModel viewModel;
    private AutoCompleteTextView spinnerPlantSelection;
    private TextInputEditText editTextTaskName, editTextNotes;
    private MaterialButton buttonSetDate, buttonSetTime, buttonSaveTask;
    private RadioGroup radioGroupRecurrence;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_custom_bottom_sheet, container, false);
        Log.d("CustomBottomSheetFragment", "onCreateView called");

        viewModel = new ViewModelProvider(requireActivity()).get(PlantTaskViewModel.class);

        spinnerPlantSelection = view.findViewById(R.id.spinner_plant_selection);
        editTextTaskName = view.findViewById(R.id.edit_text_task_name);
        editTextNotes = view.findViewById(R.id.edit_text_notes);
        buttonSetDate = view.findViewById(R.id.button_set_date);
        buttonSetTime = view.findViewById(R.id.button_set_time);
        buttonSaveTask = view.findViewById(R.id.button_save_task);
        radioGroupRecurrence = view.findViewById(R.id.radio_group_recurrence);

        buttonSetDate.setOnClickListener(v -> {
            showDatePicker();
            Log.d("CustomBottomSheetFragment", "Date button clicked");
        });

        buttonSetTime.setOnClickListener(v -> {
            showTimePicker();
            Log.d("CustomBottomSheetFragment", "Time button clicked");
        });

        buttonSaveTask.setOnClickListener(v -> {
            PlantTask task = new PlantTask(
                    System.currentTimeMillis(),
                    spinnerPlantSelection.getText().toString(),
                    editTextTaskName.getText().toString(),
                    buttonSetDate.getText().toString(),
                    buttonSetTime.getText().toString(),
                    getRecurrence(radioGroupRecurrence.getCheckedRadioButtonId()),
                    editTextNotes.getText().toString()
            );
            Log.d("CustomBottomSheetFragment", "Saving task: " + task);
            viewModel.addTask(task);
            dismiss();
        });

        return view;
    }

    private String getRecurrence(int checkedId) {
        if (checkedId == R.id.radio_button_daily) {
            return "Daily";
        } else if (checkedId == R.id.radio_button_weekly) {
            return "Weekly";
        } else {
            return "None";
        }
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year, month, dayOfMonth) -> buttonSetDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year),
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireContext(),
                (view, hourOfDay, minute) -> {
                    String amPm = (hourOfDay >= 12) ? "PM" : "AM";
                    int hour = (hourOfDay > 12) ? hourOfDay - 12 : hourOfDay;
                    hour = (hour == 0) ? 12 : hour; // handle midnight and noon
                    buttonSetTime.setText(String.format("%02d:%02d %s", hour, minute, amPm));
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false // Set to false to use 12-hour format
        );
        timePickerDialog.show();
    }

}
