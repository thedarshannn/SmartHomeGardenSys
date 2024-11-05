package ca.smartsprout.it.smart.smarthomegarden.data.model;

// Feedback.java


public class Feedback {
    private String name;
    private String email;
    private String phone;
    private float rating;
    private String description;
    public Feedback() {}
    // Constructor
    public Feedback(String name, String email, String phone, float rating, String description) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.rating = rating;
        this.description = description;
    }

    // Getters and setters (if needed)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
