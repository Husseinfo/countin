package io.github.husseinfo.countin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends Activity {
    private RecyclerView countsRecyclerView;
    private CountsListAdapter countsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.fab_add).setOnClickListener(v -> startActivity(new Intent(this, AddItemActivity.class)));

        this.countsRecyclerView = findViewById(R.id.counts);
        this.countsListAdapter = new CountsListAdapter();
        this.countsRecyclerView.setAdapter(this.countsListAdapter);
        this.countsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        this.countsRecyclerView.setHasFixedSize(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.countsListAdapter.update(Storage.getItems(this));
    }
}
