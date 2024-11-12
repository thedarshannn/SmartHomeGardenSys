package ca.smartsprout.it.smart.smarthomegarden.data.model;

public class Notification {
    private String title;
    private String message;
    private long timestamp;

    public Notification(String title, String message, long timestamp) {
        this.title = title;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
