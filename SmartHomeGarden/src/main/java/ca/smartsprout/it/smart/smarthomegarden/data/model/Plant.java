package ca.smartsprout.it.smart.smarthomegarden.data.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Plant implements Serializable {
    private String id;
    private String name;
    private String customName; // User-defined custom name for the plant
    private String description;
    private List<String> commonNames;
    private int watering;
    private Date dateAdded;

    // No-argument constructor required for Firestore
    public Plant() {
    }

    public Plant(String name, String description, List<String> commonNames, int watering) {
        this.name = name;
        this.description = description;
        this.commonNames = commonNames;
        this.watering = watering;
    }

    public Plant() {
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

    public List<String> getCommonNames() {
        return commonNames;
    }

    public void setCommonNames(List<String> commonNames) {
        this.commonNames = commonNames;
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

    public int getWatering() {
        return watering;
    }

    public void setWatering(int watering) {
        this.watering = watering;
    }
}
