package ca.smartsprout.it.smart.smarthomegarden.data.model;

import java.io.Serializable;

public class PlantTask implements Serializable {
    private long id;
    private long taskTime;
    private String plantName;
    private String taskName;
    private String date;
    private String time;
    private String recurrence;
    private String notes;
    private boolean isChecked;

    // No-argument constructor
    public PlantTask() {
    }

    // Constructor with arguments
    public PlantTask(long id, long taskTime, String plantName, String taskName, String date, String time, String recurrence, String notes) {
        this.id = id;
        this.taskTime = taskTime;
        this.plantName = plantName;
        this.taskName = taskName;
        this.date = date;
        this.time = time;
        this.recurrence = recurrence;
        this.notes = notes;
        this.isChecked = false;
    }

    // Getters and setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(long taskTime) {
        this.taskTime = taskTime;
    }

    public String getPlantName() {
        return plantName;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRecurrence() {
        return recurrence;
    }

    public void setRecurrence(String recurrence) {
        this.recurrence = recurrence;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
