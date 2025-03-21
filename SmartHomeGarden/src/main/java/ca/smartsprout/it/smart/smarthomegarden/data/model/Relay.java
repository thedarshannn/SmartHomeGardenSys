package ca.smartsprout.it.smart.smarthomegarden.data.model;
//zeel
public class Relay {
    private String state;

    // Default constructor required for Firebase
    public Relay() {}

    public Relay(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
