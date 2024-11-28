package ca.smartsprout.it.smart.smarthomegarden.ui.adapter;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.data.model.PlantTask;

public class PlantTaskAdapter extends RecyclerView.Adapter<PlantTaskAdapter.TaskViewHolder> {
    private List<PlantTask> tasks;
    private OnCheckedChangeListener onCheckedChangeListener;

    public PlantTaskAdapter(List<PlantTask> tasks) {
        this.tasks = tasks;
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        this.onCheckedChangeListener = listener;
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView plantName, taskName, date, time, recurrence, notes;
        CheckBox checkBox;

        public TaskViewHolder(View itemView) {
            super(itemView);
            plantName = itemView.findViewById(R.id.plant_name);
            taskName = itemView.findViewById(R.id.task_name);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            recurrence = itemView.findViewById(R.id.recurrence);
            notes = itemView.findViewById(R.id.notes);
            checkBox = itemView.findViewById(R.id.checkBox);
        }

        void bind(PlantTask task, OnCheckedChangeListener listener) {
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
                if (listener != null) {
                    listener.onCheckedChanged(task, isChecked);
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
        holder.bind(task, onCheckedChangeListener);
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
        }, 3000); // 3-second delay
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(PlantTask task, boolean isChecked);
    }
}
