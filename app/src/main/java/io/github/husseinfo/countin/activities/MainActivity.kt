package io.github.husseinfo.countin.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import io.github.husseinfo.countin.data.AppDatabase
import io.github.husseinfo.countin.isFirstRun
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
            MainUI(AppDatabase.getDb(baseContext)!!.countDAO()?.all!!)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        AppDatabase.getDb(applicationContext)?.close()
    }
}
