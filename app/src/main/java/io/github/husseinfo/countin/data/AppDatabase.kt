package io.github.husseinfo.countin.data

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.DocumentsContract
import androidx.core.app.ActivityCompat
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import io.github.husseinfo.countin.activities.SettingsActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

private const val dbName = "db.sqlite"

@SuppressLint("SdCardPath")
private const val DB_DIR = "/data/data/io.github.husseinfo.countin/databases/"


@Database(entities = [CountModel::class], version = 4, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun countDAO(): CountDAO?

    companion object {
        private var db: AppDatabase? = null
        fun getDb(context: Context): AppDatabase? {
            if (db == null)
                db = Room.databaseBuilder(context, AppDatabase::class.java, dbName)
                    .addMigrations(MIGRATION_1_2)
                    .addMigrations(MIGRATION_2_3)
                    .addMigrations(MIGRATION_3_4)
                    .allowMainThreadQueries()
                    .build()
            return db
        }
    }

    fun import(file: File, activityCompat: SettingsActivity) {
        db?.close()
        db = null
        val dest = File(DB_DIR, dbName)
        val importer = MainScope().launch(Dispatchers.IO) {
            db?.close()
            db = null
            val source = FileInputStream(file).channel
            val destination = FileOutputStream(dest).channel
            destination.transferFrom(source, 0, source.size())
            source.close()
            destination.close()
        }

        MainScope().launch(Dispatchers.Main) {
            importer.join()
            activityCompat.finish()
        }
    }

    fun export(context: Context) {
        db?.close()
        db = null

        val currentDB = File(DB_DIR, dbName)
        val backupDB = File(context.getExternalFilesDir(null).toString(), dbName)
        val exporter = MainScope().launch(Dispatchers.IO) {
            val source = FileInputStream(currentDB).channel
            val destination = FileOutputStream(backupDB).channel
            destination.transferFrom(source, 0, source.size())
            source.close()
            destination.close()
        }

        MainScope().launch(Dispatchers.Main) {
            exporter.join()
            db = getDb(context)
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
                putExtra(
                    DocumentsContract.EXTRA_INITIAL_URI,
                    Uri.parse("content://com.android.externalstorage.documents/document/primary:${backupDB.parent}")
                )
            }
            context.startActivity(intent)
        }
    }
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE count ADD COLUMN with_time INTEGER NOT NULL DEFAULT(0)")
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE count ADD COLUMN icon TEXT")
    }
}

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE count ADD COLUMN list TEXT")
    }
}
