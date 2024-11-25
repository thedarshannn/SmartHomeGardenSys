package ca.smartsprout.it.smart.smarthomegarden.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResponse {
    @SerializedName("entities")
    private List<PlantSearchResult> entities;

    public List<PlantSearchResult> getEntities() {
        return entities;
    }

    public void setEntities(List<PlantSearchResult> entities) {
        this.entities = entities;
    }
}