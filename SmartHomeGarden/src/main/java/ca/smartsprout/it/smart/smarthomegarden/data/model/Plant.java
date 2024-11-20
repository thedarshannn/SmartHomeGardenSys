/**
 * Smart Sprout
 * Members:
 * 1. Aditi Patel, n01525570, CENG322-RCB
 * 2. Birava Prajapati, n01579924, CENG322-RCA
 * 3. Darshankumar Prajapati, n01584247, CENG322-RCB
 * 4. Zeel Patel, n01526282, CENG322-RCB
 */
package ca.smartsprout.it.smart.smarthomegarden.data.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.annotations.JsonAdapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Plant {
    private String id;
    private String common_name;
    private String description;
    private String cycle;
    private String watering;

    @JsonAdapter(SunlightDeserializer.class)
    private List<String> sunlight;


    public Plant() {
    }

    public Plant(String id, String common_name, String description, String cycle, String watering, List<String> sunlight) {
        this.id = id;
        this.common_name = common_name;
        this.description = description;
        this.cycle = cycle;
        this.watering = watering;
        this.sunlight = sunlight;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCommon_name() {
        return common_name;
    }

    public void setCommon_name(String common_name) {
        this.common_name = common_name;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public String getWatering() {
        return watering;
    }

    public void setWatering(String watering) {
        this.watering = watering;
    }

    public List<String> getSunlight() {
        return sunlight;
    }

    public void setSunlight(List<String> sunlight) {
        this.sunlight = sunlight;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Custom Deserializer for Sunlight
    public static class SunlightDeserializer implements JsonDeserializer<List<String>> {
        @Override
        public List<String> deserialize(JsonElement json, Type typeOfT, com.google.gson.JsonDeserializationContext context) {
            List<String> result = new ArrayList<>();
            if (json.isJsonArray()) {
                JsonArray jsonArray = json.getAsJsonArray();
                for (JsonElement element : jsonArray) {
                    result.add(element.getAsString());
                }
            } else if (json.isJsonPrimitive()) {
                result.add(json.getAsString());
            }
            return result;
        }
    }
}
