package ca.smartsprout.it.smart.smarthomegarden.data.model;

public class PlantTaskHistory {
    private String id;
    private String taskName;
    private String plantName;
    private String date;
    private String time;
    private long timestamp;

    // No-argument constructor (required by Firebase)
    public PlantTaskHistory() {
        // Default constructor required for calls to DataSnapshot.getValue(PlantTaskHistory.class)
    }

    // Constructor with arguments
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