package ca.smartsprout.it.smart.smarthomegarden.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ca.smartsprout.it.smart.smarthomegarden.R;
import ca.smartsprout.it.smart.smarthomegarden.data.model.PlantTaskHistory;

public class TaskHistoryAdapter extends RecyclerView.Adapter<TaskHistoryAdapter.TaskHistoryViewHolder> {
    private List<PlantTaskHistory> taskHistoryList;
    private Context context;

    public TaskHistoryAdapter(Context context, List<PlantTaskHistory> taskHistoryList) {
        this.context = context;
        this.taskHistoryList = taskHistoryList != null ? taskHistoryList : new ArrayList<>(); // Initialize if null
    }

    @NonNull
    @Override
    public TaskHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task_history, parent, false);
        return new TaskHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHistoryViewHolder holder, int position) {
        PlantTaskHistory taskHistory = taskHistoryList.get(position);
        holder.bind(taskHistory);
    }

    @Override
    public int getItemCount() {
        return taskHistoryList != null ? taskHistoryList.size() : 0; // Handle null list
    }

    public void updateTaskHistory(List<PlantTaskHistory> newTaskHistory) {
        if (taskHistoryList == null) {
            taskHistoryList = new ArrayList<>(); // Initialize the list if null
        }
        taskHistoryList.clear(); // Clear the existing tasks
        if (newTaskHistory != null) {
            taskHistoryList.addAll(newTaskHistory); // Add the new tasks if not null
        }
        notifyDataSetChanged(); // Notify the adapter of data changes
    }

    public static class TaskHistoryViewHolder extends RecyclerView.ViewHolder {
        TextView textTaskName, textPlantName, textDate, textTime;

        public TaskHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            textTaskName = itemView.findViewById(R.id.text_task_name);
            textPlantName = itemView.findViewById(R.id.text_plant_name);
            textDate = itemView.findViewById(R.id.text_date);
            textTime = itemView.findViewById(R.id.text_time);
        }

        public void bind(PlantTaskHistory taskHistory) {
            textTaskName.setText(taskHistory.getTaskName());
            textPlantName.setText(taskHistory.getPlantName());
            textDate.setText(taskHistory.getDate());
            textTime.setText(taskHistory.getTime());
        }
    }
}