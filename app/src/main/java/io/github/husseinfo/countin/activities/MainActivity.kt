package io.github.husseinfo.countin.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import io.github.husseinfo.countin.data.AppDatabase
import io.github.husseinfo.countin.isFirstRun
import io.github.husseinfo.countin.theme.AppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

class MainActivity : ComponentActivity(), CoroutineScope by MainScope() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!isFirstRun(this)) {
            startActivity(Intent(this, IntroActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    private fun updateUI() {
        setContent {
            AppTheme {
                Column {
                    Header()
                    CountsList(items = AppDatabase.getDb(baseContext)!!.countDAO()?.all!!)
                    FAB()
                }
            }
        }
    }

    fun openSettings(view: View) = startActivity(Intent(baseContext, SettingsActivity::class.java))

    override fun onDestroy() {
        super.onDestroy()
        AppDatabase.getDb(applicationContext)?.close()
    }
}
