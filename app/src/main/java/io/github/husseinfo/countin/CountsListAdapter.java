package io.github.husseinfo.countin;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CountsListAdapter extends RecyclerView.Adapter<CountsListAdapter.ViewHolder> {

    private final List<String> items;

    public CountsListAdapter() {
        this.items = new ArrayList<>();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView count;


        public ViewHolder(View v) {
            super(v);
            count = v.findViewById(R.id.count);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_count_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {
        h.count.setText(items.get(pos));
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public void update(Set<String> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }
}
