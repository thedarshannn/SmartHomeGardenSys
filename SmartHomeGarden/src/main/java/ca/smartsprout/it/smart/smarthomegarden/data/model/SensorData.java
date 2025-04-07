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
    private float moisture;
    private String relay;
    private float UV;
    private float lux;

    // Default constructor required for Firebase
    public SensorData() {
    }

    public SensorData(float moisture, String relay, float uv, float lux) {
        this.moisture = moisture;
        this.relay = relay;
        this.UV = uv;
        this.lux = lux;
    }

    public float getMoisture() {
        return moisture;
    }

    public void setMoisture(float moisture) {
        this.moisture = moisture;
    }


    public String getRelay() {
        return relay;
    }

    public void setRelay(String relay) {
        this.relay = relay;
    }

    public float getUV() {
        return UV;
    }

    public void setUV(float uv) {
        this.UV = uv;
    }

    public float getLux() {
        return lux;
    }

    public void setLux(float lux) {
        this.lux = lux;
    }

}
