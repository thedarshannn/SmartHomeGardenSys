package ca.smartsprout.it.smart.smarthomegarden.ui.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RadioGroup;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.data.model.Plant;
import ca.smartsprout.it.smart.smarthomegarden.data.model.PlantTask;
import ca.smartsprout.it.smart.smarthomegarden.data.repository.PlantRepository;
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
    private PlantRepository plantRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_custom_bottom_sheet, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(PlantTaskViewModel.class);
        plantRepository = new PlantRepository();

        spinnerPlantSelection = view.findViewById(R.id.spinner_plant_selection);
        editTextTaskName = view.findViewById(R.id.edit_text_task_name);
        editTextNotes = view.findViewById(R.id.edit_text_notes);
        buttonSetDate = view.findViewById(R.id.button_set_date);
        buttonSetTime = view.findViewById(R.id.button_set_time);
        buttonSaveTask = view.findViewById(R.id.button_save_task);
        radioGroupRecurrence = view.findViewById(R.id.radio_group_recurrence);

        // Fetch plants and set up the spinner
        plantRepository.fetchPlants().observe(getViewLifecycleOwner(), plants -> {
            if (plants != null) {
                List<String> plantNames = new ArrayList<>();
                for (Plant plant : plants) {
                    plantNames.add(plant.getCustomName()); // Show custom name
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, plantNames);
                spinnerPlantSelection.setAdapter(adapter);
            }
        });

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
                Log.e(getString(R.string.custombottomsheetfragment), getString(R.string.task_is_null_in_oncreateview));
            }
        } else {
            Log.e(getString(R.string.custombottomsheetfragment), getString(R.string.arguments_are_null_in_oncreateview));
        }

        buttonSetDate.setOnClickListener(v -> {
            showDatePicker();
            Log.d(getString(R.string.custombottomsheetfragment), getString(R.string.date_button_clicked));
        });

        buttonSetTime.setOnClickListener(v -> {
            showTimePicker();
            Log.d(getString(R.string.custombottomsheetfragment), getString(R.string.time_button_clicked));
        });

        buttonSaveTask.setOnClickListener(v -> {
            String dateText = buttonSetDate.getText().toString();
            String timeText = buttonSetTime.getText().toString();

            if (dateText.equals("Set Date") || timeText.equals("Set Time")) {
                Toast.makeText(requireContext(), "Please set the date and time.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Calculate the task time based on the date and time set by the user
            Calendar calendar = Calendar.getInstance();
            // Assuming buttonSetDate and buttonSetTime contain the date and time in the correct format
            String[] dateParts = dateText.split("/");
            String[] timeParts = timeText.split(":| ");
            int day = Integer.parseInt(dateParts[0]);
            int month = Integer.parseInt(dateParts[1]) - 1; // Month is 0-based in Calendar
            int year = Integer.parseInt(dateParts[2]);
            int hour = Integer.parseInt(timeParts[0]);
            int minute = Integer.parseInt(timeParts[1]);
            String amPm = timeParts[2];
            if (amPm.equals(getString(R.string.pm)) && hour != 12) {
                hour += 12;
            } else if (amPm.equals(getString(R.string.am)) && hour == 12) {
                hour = 0;
            }
            calendar.set(year, month, day, hour, minute, 0);
            long taskTime = calendar.getTimeInMillis();

            String recurrence = getRecurrence(radioGroupRecurrence.getCheckedRadioButtonId());

            if (task == null) {
                // Create a new task with a unique ID
                long taskId = System.currentTimeMillis();
                PlantTask newTask = new PlantTask(
                        taskId,
                        taskTime,
                        spinnerPlantSelection.getText().toString(),
                        editTextTaskName.getText().toString(),
                        dateText,
                        timeText,
                        recurrence,
                        editTextNotes.getText().toString()
                );
                viewModel.addTask(newTask);

                // Set the task reminder with a unique request code
                setTaskReminder(taskTime, newTask.getTaskName(), (int) taskId, recurrence);

                // Create a notification to inform the user that the task has been added
                NotificationHelper.createNotification(requireContext(), getString(R.string.task_added), getString(R.string.you_have_added_a_new_task) + newTask.getTaskName());
            } else {
                // Update the existing task
                PlantTask updatedTask = new PlantTask(
                        task.getId(), // Use the existing task ID
                        taskTime,
                        spinnerPlantSelection.getText().toString(),
                        editTextTaskName.getText().toString(),
                        dateText,
                        timeText,
                        recurrence,
                        editTextNotes.getText().toString()
                );
                viewModel.updateTask(updatedTask);

                // Set the task reminder with a unique request code
                setTaskReminder(taskTime, updatedTask.getTaskName(), (int) task.getId(), recurrence);

                // Create a notification to inform the user that the task has been updated
                NotificationHelper.createNotification(requireContext(), getString(R.string.task_updated), getString(R.string.you_have_updated_the_task) + updatedTask.getTaskName());
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
                    String amPm = (hourOfDay >= 12) ? (getString(R.string.pm)) : (getString(R.string.am));
                    int hour = (hourOfDay > 12) ? hourOfDay - 12 : hourOfDay;
                    hour = (hour == 0) ? 12 : hour; // handle midnight and noon
                    buttonSetTime.setText(String.format(getString(R.string.timeformate), hour, minute, amPm));
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false // Set to false to use 12-hour format
        );
        timePickerDialog.show();
    }

    private void setTaskReminder(long taskTime, String taskName, int requestCode, String recurrence) {
        AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);

        // Reminder alarm (30 minutes before the task time)
        Intent reminderIntent = new Intent(requireContext(), AlarmReceiver.class);
        reminderIntent.putExtra(getString(R.string.task_name), taskName);
        reminderIntent.putExtra(getString(R.string.notification_type), getString(R.string.reminder));
        PendingIntent reminderPendingIntent = PendingIntent.getBroadcast(requireContext(), requestCode, reminderIntent, PendingIntent.FLAG_IMMUTABLE);
        long reminderTime = taskTime - 1800000; // 30 minutes in milliseconds
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminderTime, reminderPendingIntent);

        // Exact task time alarm
        Intent taskTimeIntent = new Intent(requireContext(), AlarmReceiver.class);
        taskTimeIntent.putExtra(getString(R.string.task_name), taskName);
        taskTimeIntent.putExtra(getString(R.string.notification_type), getString(R.string.task_time));
        PendingIntent taskTimePendingIntent = PendingIntent.getBroadcast(requireContext(), requestCode + 1, taskTimeIntent, PendingIntent.FLAG_IMMUTABLE);

        if (recurrence.equals("Daily")) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, taskTime, AlarmManager.INTERVAL_DAY, taskTimePendingIntent);
        } else if (recurrence.equals("Weekly")) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, taskTime, AlarmManager.INTERVAL_DAY * 7, taskTimePendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, taskTime, taskTimePendingIntent);
        }
    }


}



