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

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.data.model.PlantTask;
import ca.smartsprout.it.smart.smarthomegarden.utils.AlarmReceiver;
import ca.smartsprout.it.smart.smarthomegarden.utils.NotificationHelper;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.PlantTaskViewModel;

public class CustomBottomSheetFragment extends BottomSheetDialogFragment {

    private PlantTaskViewModel viewModel;
    private AutoCompleteTextView spinnerPlantSelection;
    private TextInputEditText editTextTaskName, editTextNotes;
    private MaterialButton buttonSetDate, buttonSetTime, buttonSaveTask;
    private RadioGroup radioGroupRecurrence;
    private PlantTask task;

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

        if (getArguments() != null) {
            task = (PlantTask) getArguments().getSerializable("task");
            if (task != null) {
                spinnerPlantSelection.setText(task.getPlantName());
                editTextTaskName.setText(task.getTaskName());
                editTextNotes.setText(task.getNotes());
                buttonSetDate.setText(task.getDate());
                buttonSetTime.setText(task.getTime());
                // Set recurrence radio button based on task.getRecurrence()
            } else {
                Log.e("CustomBottomSheetFragment", "Task is null in onCreateView");
            }
        } else {
            Log.e("CustomBottomSheetFragment", "Arguments are null in onCreateView");
        }

        buttonSetDate.setOnClickListener(v -> {
            showDatePicker();
            Log.d("CustomBottomSheetFragment", "Date button clicked");
        });

        buttonSetTime.setOnClickListener(v -> {
            showTimePicker();
            Log.d("CustomBottomSheetFragment", "Time button clicked");
        });

        buttonSaveTask.setOnClickListener(v -> {
            // Calculate the task time based on the date and time set by the user
            Calendar calendar = Calendar.getInstance();
            // Assuming buttonSetDate and buttonSetTime contain the date and time in the correct format
            String[] dateParts = buttonSetDate.getText().toString().split("/");
            String[] timeParts = buttonSetTime.getText().toString().split(":| ");
            int day = Integer.parseInt(dateParts[0]);
            int month = Integer.parseInt(dateParts[1]) - 1; // Month is 0-based in Calendar
            int year = Integer.parseInt(dateParts[2]);
            int hour = Integer.parseInt(timeParts[0]);
            int minute = Integer.parseInt(timeParts[1]);
            String amPm = timeParts[2];
            if (amPm.equals("PM") && hour != 12) {
                hour += 12;
            } else if (amPm.equals("AM") && hour == 12) {
                hour = 0;
            }
            calendar.set(year, month, day, hour, minute, 0);
            long taskTime = calendar.getTimeInMillis();

            if (task == null) {
                // Create a new task with a unique ID
                long taskId = System.currentTimeMillis(); // Use current time as a unique ID
                PlantTask newTask = new PlantTask(
                        taskId,
                        taskTime,
                        spinnerPlantSelection.getText().toString(),
                        editTextTaskName.getText().toString(),
                        buttonSetDate.getText().toString(),
                        buttonSetTime.getText().toString(),
                        getRecurrence(radioGroupRecurrence.getCheckedRadioButtonId()),
                        editTextNotes.getText().toString()
                );
                Log.d("CustomBottomSheetFragment", "Saving new task: " + newTask);
                viewModel.addTask(newTask);

                // Set the task reminder with a unique request code
                setTaskReminder(taskTime, newTask.getTaskName(), (int) taskId);

                // Create a notification to inform the user that the task has been added
                NotificationHelper.createNotification(requireContext(), "Task Added", "You have added a new task: " + newTask.getTaskName());
            } else {
                // Update the existing task
                PlantTask updatedTask = new PlantTask(
                        task.getId(), // Use the existing task ID
                        taskTime,
                        spinnerPlantSelection.getText().toString(),
                        editTextTaskName.getText().toString(),
                        buttonSetDate.getText().toString(),
                        buttonSetTime.getText().toString(),
                        getRecurrence(radioGroupRecurrence.getCheckedRadioButtonId()),
                        editTextNotes.getText().toString()
                );
                Log.d("CustomBottomSheetFragment", "Updating task: " + updatedTask);
                viewModel.updateTask(updatedTask);

                // Set the task reminder with a unique request code
                setTaskReminder(taskTime, updatedTask.getTaskName(), (int) task.getId());

                // Create a notification to inform the user that the task has been updated
                NotificationHelper.createNotification(requireContext(), "Task Updated", "You have updated the task: " + updatedTask.getTaskName());
            }

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
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
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

    private void setTaskReminder(long taskTime, String taskName, int requestCode) {
        AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);

        // Reminder alarm (30 minutes before the task time)
        Intent reminderIntent = new Intent(requireContext(), AlarmReceiver.class);
        reminderIntent.putExtra("task_name", taskName);
        reminderIntent.putExtra("notification_type", "reminder");
        PendingIntent reminderPendingIntent = PendingIntent.getBroadcast(requireContext(), requestCode, reminderIntent, PendingIntent.FLAG_IMMUTABLE);
        long reminderTime = taskTime - 1800000; // 30 minutes in milliseconds
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminderTime, reminderPendingIntent);

        // Exact task time alarm
        Intent taskTimeIntent = new Intent(requireContext(), AlarmReceiver.class);
        taskTimeIntent.putExtra("task_name", taskName);
        taskTimeIntent.putExtra("notification_type", "task_time");
        PendingIntent taskTimePendingIntent = PendingIntent.getBroadcast(requireContext(), requestCode + 1, taskTimeIntent, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, taskTime, taskTimePendingIntent);
    }
}



