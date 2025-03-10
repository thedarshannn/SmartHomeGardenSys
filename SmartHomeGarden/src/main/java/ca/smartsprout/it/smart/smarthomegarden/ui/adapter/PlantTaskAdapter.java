/**
 * Smart Sprout
 * Members:
 * 1. Aditi Patel, n01525570, CENG322-RCB
 * 2. Birava Prajapati, n01579924, CENG322-RCA
 * 3. Darshankumar Prajapati, n01574247, CENG322-RCB
 * 4. Zeel Patel, n01526282, CENG322-RCB
 */

package ca.smartsprout.it.smart.smarthomegarden.ui.adapter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.data.model.PlantTask;
import ca.smartsprout.it.smart.smarthomegarden.data.model.PlantTaskHistory;
import ca.smartsprout.it.smart.smarthomegarden.utils.AlarmReceiver;
import ca.smartsprout.it.smart.smarthomegarden.viewmodels.TaskHistoryViewModel;

public class PlantTaskAdapter extends RecyclerView.Adapter<PlantTaskAdapter.TaskViewHolder> {
    private List<PlantTask> tasks;
    private OnCheckedChangeListener onCheckedChangeListener;
    private OnEditButtonClickListener onEditButtonClickListener;
    private Context context;
    private TaskHistoryViewModel taskHistoryViewModel;

    // Constructor
    public PlantTaskAdapter(Context context, List<PlantTask> tasks, TaskHistoryViewModel taskHistoryViewModel) {
        this.context = context;
        this.tasks = tasks != null ? tasks : new ArrayList<>(); // Initialize tasks if null
        this.taskHistoryViewModel = taskHistoryViewModel;
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        this.onCheckedChangeListener = listener;
    }

    public void setOnEditButtonClickListener(OnEditButtonClickListener listener) {
        this.onEditButtonClickListener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plant_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        PlantTask task = tasks.get(position);
        holder.bind(task, onCheckedChangeListener, onEditButtonClickListener, context);

        // Handle CheckBox click
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            task.setChecked(isChecked); // Update task state

            if (isChecked) {
                // Move the task to the task history
                PlantTaskHistory taskHistory = new PlantTaskHistory(
                        String.valueOf(task.getId()), // Use task ID as history ID
                        task.getTaskName(),
                        task.getPlantName(),
                        task.getDate(),
                        task.getTime(),
                        System.currentTimeMillis() // Current timestamp
                );
                taskHistoryViewModel.addTaskHistory(taskHistory);

                // Notify the listener (if any)
                if (onCheckedChangeListener != null) {
                    onCheckedChangeListener.onCheckedChanged(task, isChecked);
                }

                // Remove the task from the current list immediately
                tasks.remove(task);
                notifyDataSetChanged();

                // Cancel the task reminder
                cancelTaskReminder(task);

                // Show a toast message
                Toast.makeText(context, "Your Task is complete", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks != null ? tasks.size() : 0; // Handle null tasks list
    }

    public void updateTasks(List<PlantTask> newTasks) {
        if (tasks == null) {
            tasks = new ArrayList<>(); // Initialize tasks if null
        }
        tasks.clear(); // Clear the existing tasks
        tasks.addAll(newTasks); // Add the new tasks
        notifyDataSetChanged(); // Notify the adapter of data changes
    }

    public void removeTask(PlantTask task) {
        if (tasks != null) {
            tasks.remove(task); // Remove the task
            notifyDataSetChanged(); // Notify the adapter of data changes
        }
    }

    private void cancelTaskReminder(PlantTask task) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Cancel reminder alarm
        Intent reminderIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent reminderPendingIntent = PendingIntent.getBroadcast(context, (int) task.getId(), reminderIntent, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.cancel(reminderPendingIntent);

        // Cancel exact task time alarm
        Intent taskTimeIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent taskTimePendingIntent = PendingIntent.getBroadcast(context, (int) task.getId() + 1, taskTimeIntent, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.cancel(taskTimePendingIntent);
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView plantName, taskName, date, time, recurrence, notes;
        CheckBox checkBox;
        ImageView editButton;

        public TaskViewHolder(View itemView) {
            super(itemView);
            plantName = itemView.findViewById(R.id.plant_name);
            taskName = itemView.findViewById(R.id.task_name);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            recurrence = itemView.findViewById(R.id.recurrence);
            notes = itemView.findViewById(R.id.notes);
            checkBox = itemView.findViewById(R.id.checkBox);
            editButton = itemView.findViewById(R.id.edit_button);
        }

        void bind(PlantTask task, OnCheckedChangeListener checkedChangeListener, OnEditButtonClickListener editButtonClickListener, Context context) {
            plantName.setText(task.getPlantName());
            taskName.setText(task.getTaskName());
            date.setText(task.getDate());
            time.setText(task.getTime());
            recurrence.setText(task.getRecurrence());
            notes.setText(task.getNotes());

            checkBox.setOnCheckedChangeListener(null); // Clear previous listener
            checkBox.setChecked(task.isChecked()); // Set checkbox state based on task
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                task.setChecked(isChecked); // Update task state
                if (checkedChangeListener != null) {
                    checkedChangeListener.onCheckedChanged(task, isChecked);
                }
            });

            editButton.setOnClickListener(v -> {
                if (editButtonClickListener != null) {
                    editButtonClickListener.onEditButtonClick(task);
                }
            });
        }
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(PlantTask task, boolean isChecked);
    }

    public interface OnEditButtonClickListener {
        void onEditButtonClick(PlantTask task);
    }
}