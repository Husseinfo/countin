package io.github.husseinfo.countin.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.husseinfo.countin.CountsListAdapter
import io.github.husseinfo.countin.R
import io.github.husseinfo.countin.data.AppDatabase
import io.github.husseinfo.countin.isFirstRun
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    private lateinit var countsRecyclerView: RecyclerView
    private lateinit var countsListAdapter: CountsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!isFirstRun(this)) {
            startActivity(Intent(this, IntroActivity::class.java))
        }

        findViewById<View>(R.id.fab_add).setOnClickListener {
            startActivity(Intent(this, AddItemActivity::class.java))
        }

        countsRecyclerView = findViewById(R.id.counts)
        countsListAdapter = CountsListAdapter()
        countsRecyclerView.adapter = countsListAdapter
        countsRecyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        countsRecyclerView.setHasFixedSize(false)
    }

    override fun onResume() {
        super.onResume()

        val items = async(Dispatchers.IO) {
            AppDatabase.getDb(baseContext)!!.countDAO()?.all!!
        }

        launch(Dispatchers.Main) {
            countsListAdapter.update(items.await())
        }
    }

    fun openSettings(view: View) = startActivity(Intent(baseContext, SettingsActivity::class.java))

    override fun onDestroy() {
        super.onDestroy()
        AppDatabase.getDb(applicationContext)?.close()
    }
}
