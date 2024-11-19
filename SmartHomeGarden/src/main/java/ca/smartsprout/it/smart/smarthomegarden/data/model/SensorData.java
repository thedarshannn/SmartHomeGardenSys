/**
 * Smart Sprout
 * Members:
 * 1. Aditi Patel, n01525570, CENG322-RCB
 * 2. Birava Prajapati, n01579924, CENG322-RCA
 * 3. Darshankumar Prajapati, n01584247, CENG322-RCB
 * 4. Zeel Patel, n01526282, CENG322-RCB
 */
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
