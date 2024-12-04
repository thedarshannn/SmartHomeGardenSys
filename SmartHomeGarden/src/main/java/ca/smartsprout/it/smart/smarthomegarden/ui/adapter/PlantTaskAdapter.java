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
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.data.model.PlantTask;
import ca.smartsprout.it.smart.smarthomegarden.utils.AlarmReceiver;

public class PlantTaskAdapter extends RecyclerView.Adapter<PlantTaskAdapter.TaskViewHolder> {
    private List<PlantTask> tasks;
    private OnCheckedChangeListener onCheckedChangeListener;
    private OnEditButtonClickListener onEditButtonClickListener;
    private Context context;

    public PlantTaskAdapter(Context context, List<PlantTask> tasks) {
        this.context = context;
        this.tasks = tasks;
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        this.onCheckedChangeListener = listener;
    }

    public void setOnEditButtonClickListener(OnEditButtonClickListener listener) {
        this.onEditButtonClickListener = listener;
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
                if (isChecked) {
                    Toast.makeText(context, "Your Task is complete", Toast.LENGTH_SHORT).show();
                }
            });

            editButton.setOnClickListener(v -> {
                if (editButtonClickListener != null) {
                    editButtonClickListener.onEditButtonClick(task);
                }
            });
        }
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plant_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        PlantTask task = tasks.get(position);
        holder.bind(task, onCheckedChangeListener, onEditButtonClickListener, context);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void updateTasks(List<PlantTask> newTasks) {
        tasks.clear();
        tasks.addAll(newTasks);
        notifyDataSetChanged();
    }

    public void removeTask(PlantTask task) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            tasks.remove(task);
            notifyDataSetChanged();
            cancelTaskReminder(task); // Cancel the reminder when the task is removed
        }, 3000); // 3-second delay
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


    public interface OnCheckedChangeListener {
        void onCheckedChanged(PlantTask task, boolean isChecked);
    }

    public interface OnEditButtonClickListener {
        void onEditButtonClick(PlantTask task);
    }
}
