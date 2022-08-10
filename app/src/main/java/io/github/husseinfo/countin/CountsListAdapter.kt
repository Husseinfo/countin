package io.github.husseinfo.countin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.github.husseinfo.countin.data.CountModel

class CountsListAdapter : RecyclerView.Adapter<CountsListAdapter.ViewHolder>() {
    private val items: MutableList<CountModel> = ArrayList()

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var title: TextView
        var years: TextView
        var months: TextView
        var days: TextView

        init {
            title = v.findViewById(R.id.tv_title)
            years = v.findViewById(R.id.tv_years)
            months = v.findViewById(R.id.tv_months)
            days = v.findViewById(R.id.tv_days)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_count_item, parent, false)
        )
    }

    override fun onBindViewHolder(h: ViewHolder, pos: Int) {
        val c = items[pos]
        h.title.text = c.title
        h.years.text = c.years
        h.months.text = c.months
        h.days.text = c.days
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun update(items: List<CountModel>?) {
        this.items.clear()
        this.items.addAll(items!!)
        notifyDataSetChanged()
    }
}