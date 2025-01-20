/**
 * Smart Sprout
 * Members:
 * 1. Aditi Patel, n01525570, CENG322-RCB
 * 2. Birava Prajapati, n01579924, CENG322-RCA
 * 3. Darshankumar Prajapati, n01574247, CENG322-RCB
 * 4. Zeel Patel, n01526282, CENG322-RCB
 */

package ca.smartsprout.it.smart.smarthomegarden.data.model;

import java.io.Serializable;
import java.util.Date;

public class Plant implements Serializable {
    private String id;
    private String name;
    private String customName; // User-defined custom name for the plant
    private String description;
    private String wateringPeriod;
    private Date dateAdded;
    private String toxicity;
    private String suitability;

    // No-argument constructor required for Firestore
    public Plant() {
    }

    public Plant(String name, String description, String wateringPeriod, String toxicity, String suitability) {
        this.name = name;
        this.description = description;
        this.wateringPeriod = wateringPeriod;
        this.toxicity = toxicity;
        this.suitability = suitability;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWateringPeriod() {
        return wateringPeriod;
    }

    public void setWateringPeriod(String wateringPeriod) {
        this.wateringPeriod = wateringPeriod;
    }

    public String getToxicity() {
        return toxicity;
    }

    public void setToxicity(String toxicity) {
        this.toxicity = toxicity;
    }

    public String getSuitability() {
        return suitability;
    }

    public void setSuitability(String suitability) {
        this.suitability = suitability;
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    @Override
    public String toString() {
        return "Plant{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", customName='" + customName + '\'' +
                ", description='" + description + '\'' +
                ", wateringPeriod='" + wateringPeriod + '\'' +
                ", dateAdded=" + dateAdded +
                ", toxicity='" + toxicity + '\'' +
                ", suitability='" + suitability + '\'' +
                '}';
    }
}
