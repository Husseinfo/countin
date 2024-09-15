package io.github.husseinfo.countin.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import io.github.husseinfo.countin.data.AppDatabase
import java.io.File
import java.io.FileOutputStream

private val REQUEST_CODE_OPEN_FILE: Int = 1

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SettingsUI(
                onImportClick = { import() }
            )
        }
    }

    fun import() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
        }
        startActivityForResult(intent, REQUEST_CODE_OPEN_FILE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_OPEN_FILE && resultCode == RESULT_OK) {
            data?.data?.also { uri ->
                val contentResolver = contentResolver
                contentResolver.openInputStream(uri)?.use { inputStream ->
                    val file = File(cacheDir, "tempFile")
                    FileOutputStream(file).use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                    AppDatabase.getDb(this)?.import(file, this)
                }
            }
        }
    }
}
