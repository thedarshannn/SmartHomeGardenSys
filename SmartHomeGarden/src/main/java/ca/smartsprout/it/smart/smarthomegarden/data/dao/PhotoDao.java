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
