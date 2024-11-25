package ca.smartsprout.it.smart.smarthomegarden.data.model;

import com.google.gson.annotations.SerializedName;

public class PlantSearchResult {
    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("matched_in")
    private String matchedIn;

    @SerializedName("matched_in_type")
    private String matchedInType;

    @SerializedName("entity_name")
    private String entityName;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getMatchedIn() {
        return matchedIn;
    }

    public void setMatchedIn(String matchedIn) {
        this.matchedIn = matchedIn;
    }

    public String getMatchedInType() {
        return matchedInType;
    }

    public void setMatchedInType(String matchedInType) {
        this.matchedInType = matchedInType;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }
}
