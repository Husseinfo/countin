package io.github.husseinfo.countin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.husseinfo.countin.data.AppDatabase
import kotlinx.coroutines.*

class MainActivity : Activity(), CoroutineScope by MainScope() {
    private lateinit var countsRecyclerView: RecyclerView
    private lateinit var countsListAdapter: CountsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.fab_add).setOnClickListener { v: View? ->
            startActivity(
                Intent(
                    this,
                    AddItemActivity::class.java
                )
            )
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
}
