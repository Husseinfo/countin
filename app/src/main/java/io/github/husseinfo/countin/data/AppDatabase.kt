package io.github.husseinfo.countin.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CountModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun countDAO(): CountDAO?

    companion object {
        private var db: AppDatabase? = null
        fun getDb(context: Context): AppDatabase? {
            if (db == null)
                db = Room.databaseBuilder(context, AppDatabase::class.java, "db.sqlite").build()
            return db
        }
    }
}
