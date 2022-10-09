package io.github.husseinfo.countin

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.recyclerview.widget.RecyclerView
import io.github.husseinfo.countin.activities.AddItemActivity
import io.github.husseinfo.countin.activities.EDIT_RECORD_ID
import io.github.husseinfo.countin.data.AppDatabase
import io.github.husseinfo.countin.data.CountModel
import io.github.husseinfo.maticonsearch.getIcon
import io.github.husseinfo.maticonsearch.getIconByName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.time.Period

class CountsListAdapter : RecyclerView.Adapter<CountsListAdapter.ViewHolder>() {
    private val items: MutableList<CountModel> = ArrayList()

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var title: TextView
        var years: TextView
        var months: TextView
        var days: TextView
        var hours: TextView
        var minutes: TextView
        var icon: ComposeView

        init {
            title = v.findViewById(R.id.tv_title)
            years = v.findViewById(R.id.tv_years)
            months = v.findViewById(R.id.tv_months)
            days = v.findViewById(R.id.tv_days)
            hours = v.findViewById(R.id.tv_hours)
            minutes = v.findViewById(R.id.tv_minutes)
            icon = v.findViewById(R.id.icon)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_count_item, parent, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(h: ViewHolder, pos: Int) {
        val c = items[pos]
        h.title.text = c.title
        val difference: Period = c.dateDiff();
        h.years.text = difference.years.toString()
        h.months.text = difference.months.toString()
        h.days.text = difference.days.toString()

        if (c.icon != null) {
            val icon = getIconByName(h.itemView.context, c.icon)
            h.icon.setContent {
                Icon(
                    modifier = Modifier.size(40.dp),
                    imageVector = icon,
                    contentDescription = c.icon
                )
            }
        } else {
            h.icon.setContent {
                Icon(
                    modifier = Modifier.size(40.dp),
                    imageVector = getIcon(
                        h.itemView.context,
                        "CalendarMonth",
                        Icons.Filled
                    ),
                    contentDescription = "CalendarMonth"
                )
            }
        }

        if (c.withTime) {
            h.hours.text = (c.milliDiff() / 1000 / 3600 % 24).toString()
            h.minutes.text = (c.milliDiff() / 1000 / 60 % 60).toString()
            if (h.minutes.text.length == 1)
                h.minutes.text = "0" + h.minutes.text
            h.itemView.findViewById<View>(R.id.ll_time).visibility = View.VISIBLE
        }

        h.itemView.setOnClickListener {
            val intent = Intent(h.itemView.context, AddItemActivity::class.java)
            intent.putExtra(EDIT_RECORD_ID, c.id)
            h.itemView.context.startActivity(intent)
        }

        h.itemView.setOnLongClickListener {
            AlertDialog.Builder(h.itemView.context)
                .setTitle(R.string.confirm_delete)
                .setPositiveButton(R.string.delete) { _: DialogInterface?, _: Int ->

                    MainScope().launch(Dispatchers.IO) {
                        AppDatabase.getDb(h.itemView.context)?.countDAO()?.delete(c)
                    }
                    items.removeAt(pos)
                    notifyItemRemoved(pos)
                    notifyItemRangeChanged(pos, itemCount);
                }
                .setNegativeButton(R.string.dismiss) { _: DialogInterface?, _: Int -> }
                .create().show()
            true
        }
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        holder.itemView.findViewById<View>(R.id.ll_time).visibility = View.GONE
        holder.icon.setContent { }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun update(items: List<CountModel>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }
}
