package ca.smartsprout.it.smart.smarthomegarden.data;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import ca.smartsprout.it.smart.smarthomegarden.data.dao.PhotoDao;
import ca.smartsprout.it.smart.smarthomegarden.data.model.Photo;

@Database(entities = {Photo.class}, version = 1)
public abstract class PhotoDatabase extends RoomDatabase {

    private static PhotoDatabase instance;

    public abstract PhotoDao photoDao();

    public static synchronized PhotoDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            PhotoDatabase.class, "photo_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}

