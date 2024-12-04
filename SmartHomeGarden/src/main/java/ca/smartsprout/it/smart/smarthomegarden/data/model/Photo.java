/**
 * Smart Sprout
 * Members:
 * 1. Aditi Patel, n01525570, CENG322-RCB
 * 2. Birava Prajapati, n01579924, CENG322-RCA
 * 3. Darshankumar Prajapati, n01574247, CENG322-RCB
 * 4. Zeel Patel, n01526282, CENG322-RCB
 */
package ca.smartsprout.it.smart.smarthomegarden.data.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "photos")
public class Photo {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @NonNull
    private String url;
    private String name;
    private String date;
    private boolean isSynced;

    // Constructor
    public Photo(@NonNull String url, String date) {
        this.url = url;
        this.date = date;
        this.isSynced = false;
    }

    // Getters
    public long getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    // Setters
    public void setId(long id) {
        this.id = id;
    }

    public void setUrl(@NonNull String url) {
        this.url = url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isSynced() {
        return isSynced;
    }

    public void setSynced(boolean synced) {
        this.isSynced = synced;
    }
}