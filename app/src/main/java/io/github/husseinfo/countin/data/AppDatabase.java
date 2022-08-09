package io.github.husseinfo.countin.data;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {CountModel.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase db;

    public abstract CountDAO countDAO();

    public static AppDatabase getDb(Context context) {
        if (db == null)
            db = Room.databaseBuilder(context, AppDatabase.class, "db.sqlite")
                    .allowMainThreadQueries().build();
        return db;
    }
}
