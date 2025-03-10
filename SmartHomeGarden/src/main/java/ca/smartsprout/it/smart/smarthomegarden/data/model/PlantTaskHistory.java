package ca.smartsprout.it.smart.smarthomegarden.data.model;

public class PlantTaskHistory {
    private String id; // Unique identifier for the task history
    private String taskName; // Name of the task
    private String plantName; // Name of the plant associated with the task
    private String date; // Date when the task was completed
    private String time; // Time when the task was completed
    private long timestamp; // Timestamp for sorting

    // Constructor
    public PlantTaskHistory(String id, String taskName, String plantName, String date, String time, long timestamp) {
        this.id = id;
        this.taskName = taskName;
        this.plantName = plantName;
        this.date = date;
        this.time = time;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getPlantName() {
        return plantName;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}