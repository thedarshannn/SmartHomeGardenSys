package ca.smartsprout.it.smart.smarthomegarden.data.model;

public class SensorData {
    private double sunlight;
    private double temperature;
    private double moisture;

    public SensorData() {
        // Default constructor required for calls to DataSnapshot.getValue(SensorData.class)
    }

    public SensorData(double sunlight, double temperature, double moisture) {
        this.sunlight = sunlight;
        this.temperature = temperature;
        this.moisture = moisture;
    }

    public double getSunlight() {
        return sunlight;
    }

    public void setSunlight(double sunlight) {
        this.sunlight = sunlight;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getMoisture() {
        return moisture;
    }

    public void setMoisture(double moisture) {
        this.moisture = moisture;
    }
}
