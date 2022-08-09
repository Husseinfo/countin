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
        public TextView title;
        public TextView years;
        public TextView months;
        public TextView days;

        public ViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.tv_title);
            years = v.findViewById(R.id.tv_years);
            months = v.findViewById(R.id.tv_months);
            days = v.findViewById(R.id.tv_days);
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
        CountModel c = CountModel.load(items.get(pos));
        h.title.setText(c.getTitle());
        h.years.setText(c.getYears());
        h.months.setText(c.getMonths());
        h.days.setText(c.getDays());
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
