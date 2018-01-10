package Databases.ROOM;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by Larisa on 09.01.2018.
 */

@Database(entities = {SMS.class,CALL.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;
    public abstract SMSDao smsDao();

    public abstract CallDao callDao();

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "my-database")
                            // allow queries on the main thread.
                            // Don't do this on a real app! See PersistenceBasicSample for an example.
                    //todo:change these settings
                            .allowMainThreadQueries()
                            .build();
            }
            return INSTANCE;
        }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}
