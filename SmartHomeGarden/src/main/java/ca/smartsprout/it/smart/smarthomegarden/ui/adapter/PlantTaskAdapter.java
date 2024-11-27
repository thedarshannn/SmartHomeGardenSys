package ca.smartsprout.it.smart.smarthomegarden.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.data.model.PlantTask;

public class PlantTaskAdapter extends RecyclerView.Adapter<PlantTaskAdapter.TaskViewHolder> {
    private List<PlantTask> tasks;

    public PlantTaskAdapter(List<PlantTask> tasks) {
        this.tasks = tasks;
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView plantName, taskName, date, time, recurrence, notes;

        public TaskViewHolder(View itemView) {
            super(itemView);
            plantName = itemView.findViewById(R.id.plant_name);
            taskName = itemView.findViewById(R.id.task_name);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            recurrence = itemView.findViewById(R.id.recurrence);
            notes = itemView.findViewById(R.id.notes);
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
        holder.plantName.setText(task.getPlantName());
        holder.taskName.setText(task.getTaskName());
        holder.date.setText(task.getDate());
        holder.time.setText(task.getTime());
        holder.recurrence.setText(task.getRecurrence());
        holder.notes.setText(task.getNotes());
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
}
