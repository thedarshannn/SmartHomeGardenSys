/**
 * Smart Sprout
 * Members:
 * 1. Aditi Patel, n01525570, CENG322-RCB
 * 2. Birava Prajapati, n01579924, CENG322-RCA
 * 3. Darshankumar Prajapati, n01584247, CENG322-RCB
 * 4. Zeel Patel, n01526282, CENG322-RCB
 */
package ca.smartsprout.it.smart.smarthomegarden.data.model;

import java.util.HashMap;
import java.util.Map;

public class SensorData {
    private float moisture;
    private float temperature;
    private String relay;
    private Map<String, Float> lightSensor;

    // Default constructor required for Firebase
    public SensorData() {
    }

    public SensorData(float moisture, float temperature, String relay, float uv, float lux) {
        this.moisture = moisture;
        this.temperature = temperature;
        this.relay = relay;
        this.lightSensor = new HashMap<>();
        this.lightSensor.put("UV", uv);
        this.lightSensor.put("lux", lux);
    }

    public float getMoisture() {
        return moisture;
    }

    public void setMoisture(float moisture) {
        this.moisture = moisture;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public String getRelay() {
        return relay;
    }

    public void setRelay(String relay) {
        this.relay = relay;
    }

    public Map<String, Float> getLightSensor() {
        return lightSensor;
    }

    public void setLightSensor(Map<String, Float> lightSensor) {
        this.lightSensor = lightSensor;
    }
}
