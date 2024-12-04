/**
 * Smart Sprout
 * Members:
 * 1. Aditi Patel, n01525570, CENG322-RCB
 * 2. Birava Prajapati, n01579924, CENG322-RCA
 * 3. Darshankumar Prajapati, n01574247, CENG322-RCB
 * 4. Zeel Patel, n01526282, CENG322-RCB
 */

package ca.smartsprout.it.smart.smarthomegarden.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ca.smartsprout.it.smart.smarthomegarden.data.model.Photo;

@Dao
public interface PhotoDao {

    @Insert
    void insertPhoto(Photo photo);

    @Query("SELECT * FROM photos")
    LiveData<List<Photo>> getAllPhotos();

    @Query("SELECT * FROM photos WHERE isSynced = 0")
    List<Photo> getAllUnsyncedPhotos(); // Get unsynced photos

    @Update
    void updatePhoto(Photo photo); // Mark photo as synced
}
