package ca.smartsprout.it.smart.smarthomegarden.data.model;

public class Notification {
    private String id;
    private String title;
    private String message;
    private long timestamp;

    public Notification() {
        // Default constructor required for calls to DataSnapshot.getValue(Notification.class)
    }

    public Notification(String id, String title, String message, long timestamp) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
