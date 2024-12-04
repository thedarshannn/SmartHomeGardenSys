/**
 * Smart Sprout
 * Members:
 * 1. Aditi Patel, n01525570, CENG322-RCB
 * 2. Birava Prajapati, n01579924, CENG322-RCA
 * 3. Darshankumar Prajapati, n01584247, CENG322-RCB
 * 4. Zeel Patel, n01526282, CENG322-RCB
 */

package ca.smartsprout.it.smart.smarthomegarden.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PlantDetail implements Serializable {

    @SerializedName("common_names")
    private List<String> commonNames;

    @SerializedName("taxonomy")
    private Taxonomy taxonomy;

    @SerializedName("url")
    private String url;

    @SerializedName("gbif_id")
    private int gbifId;

    @SerializedName("inaturalist_id")
    private int inaturalistId;

    @SerializedName("rank")
    private String rank;

    @SerializedName("description")
    private Description description;

    @SerializedName("synonyms")
    private List<String> synonyms;

    @SerializedName("image")
    private Image image;

    @SerializedName("edible_parts")
    private List<String> edibleParts;

    @SerializedName("watering")
    private Watering watering;

    @SerializedName("propagation_methods")
    private List<String> propagationMethods;

    @SerializedName("language")
    private String language;

    @SerializedName("entity_id")
    private String entityId;

    @SerializedName("name")
    private String name;

    // Getters and setters
    public List<String> getCommonNames() {
        return commonNames;
    }

    public void setCommonNames(List<String> commonNames) {
        this.commonNames = commonNames;
    }

    public Taxonomy getTaxonomy() {
        return taxonomy;
    }

    public void setTaxonomy(Taxonomy taxonomy) {
        this.taxonomy = taxonomy;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getGbifId() {
        return gbifId;
    }

    public void setGbifId(int gbifId) {
        this.gbifId = gbifId;
    }

    public int getInaturalistId() {
        return inaturalistId;
    }

    public void setInaturalistId(int inaturalistId) {
        this.inaturalistId = inaturalistId;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    public List<String> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(List<String> synonyms) {
        this.synonyms = synonyms;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public List<String> getEdibleParts() {
        return edibleParts;
    }

    public void setEdibleParts(List<String> edibleParts) {
        this.edibleParts = edibleParts;
    }

    public Watering getWatering() {
        return watering;
    }

    public void setWatering(Watering watering) {
        this.watering = watering;
    }

    public List<String> getPropagationMethods() {
        return propagationMethods;
    }

    public void setPropagationMethods(List<String> propagationMethods) {
        this.propagationMethods = propagationMethods;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Plant toPlant() {
        return new Plant(
                name,
                description.getValue(),
                commonNames,
                watering.getMax()
        );
    }

    // Nested classes
    public static class Taxonomy {
        @SerializedName("class")
        private String className;

        @SerializedName("genus")
        private String genus;

        @SerializedName("order")
        private String order;

        @SerializedName("family")
        private String family;

        @SerializedName("phylum")
        private String phylum;

        @SerializedName("kingdom")
        private String kingdom;

        // Getters and setters
        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getGenus() {
            return genus;
        }

        public void setGenus(String genus) {
            this.genus = genus;
        }

        public String getOrder() {
            return order;
        }

        public void setOrder(String order) {
            this.order = order;
        }

        public String getFamily() {
            return family;
        }

        public void setFamily(String family) {
            this.family = family;
        }

        public String getPhylum() {
            return phylum;
        }

        public void setPhylum(String phylum) {
            this.phylum = phylum;
        }

        public String getKingdom() {
            return kingdom;
        }

        public void setKingdom(String kingdom) {
            this.kingdom = kingdom;
        }
    }

    public static class Description {
        @SerializedName("value")
        private String value;

        @SerializedName("citation")
        private String citation;

        @SerializedName("license_name")
        private String licenseName;

        @SerializedName("license_url")
        private String licenseUrl;

        // Getters and setters
        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getCitation() {
            return citation;
        }

        public void setCitation(String citation) {
            this.citation = citation;
        }

        public String getLicenseName() {
            return licenseName;
        }

        public void setLicenseName(String licenseName) {
            this.licenseName = licenseName;
        }

        public String getLicenseUrl() {
            return licenseUrl;
        }

        public void setLicenseUrl(String licenseUrl) {
            this.licenseUrl = licenseUrl;
        }
    }

    public static class Image {
        @SerializedName("value")
        private String value;

        @SerializedName("citation")
        private String citation;

        @SerializedName("license_name")
        private String licenseName;

        @SerializedName("license_url")
        private String licenseUrl;

        // Getters and setters
        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getCitation() {
            return citation;
        }

        public void setCitation(String citation) {
            this.citation = citation;
        }

        public String getLicenseName() {
            return licenseName;
        }

        public void setLicenseName(String licenseName) {
            this.licenseName = licenseName;
        }

        public String getLicenseUrl() {
            return licenseUrl;
        }

        public void setLicenseUrl(String licenseUrl) {
            this.licenseUrl = licenseUrl;
        }
    }

    public static class Watering {
        @SerializedName("max")
        private int max;

        @SerializedName("min")
        private int min;

        // Getters and setters
        public int getMax() {
            return max;
        }

        public void setMax(int max) {
            this.max = max;
        }

        public int getMin() {
            return min;
        }

        public void setMin(int min) {
            this.min = min;
        }
    }
}
