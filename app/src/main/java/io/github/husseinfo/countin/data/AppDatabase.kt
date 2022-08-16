package io.github.husseinfo.countin.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [CountModel::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun countDAO(): CountDAO?

    companion object {
        private var db: AppDatabase? = null
        fun getDb(context: Context): AppDatabase? {
            if (db == null)
                db = Room.databaseBuilder(context, AppDatabase::class.java, "db.sqlite")
                    .addMigrations(MIGRATION_1_2)
                    .build()
            return db
        }
    }
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE count ADD COLUMN with_time INTEGER NOT NULL DEFAULT(0)")
    }
}
