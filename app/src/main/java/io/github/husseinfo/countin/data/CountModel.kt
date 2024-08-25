package io.github.husseinfo.countin.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.husseinfo.countin.activities.format
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.ZoneOffset
import java.util.Calendar
import java.util.Date

@Entity(tableName = "count")
class CountModel(
    @field:ColumnInfo(name = "title") var title: String,
    @field:ColumnInfo(name = "date") var date: Long,
    @field:ColumnInfo(name = "with_time") val withTime: Boolean,
    @field:ColumnInfo(name = "icon") val icon: String?,
    @field:ColumnInfo(name = "list") val list: String?,
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id = 0

    fun dateDiff(): Period {
        return Period.between(
            LocalDateTime.ofInstant(
                Instant.ofEpochMilli(date),
                ZoneOffset.systemDefault()
            ).toLocalDate(),
            LocalDate.now(ZoneOffset.systemDefault())
        )
    }

    private fun milliDiff(): Long {
        return Calendar.getInstance().time.time - date
    }

    fun formatDate(): CharSequence {
        val c = Calendar.getInstance()
        c.time = Date.from(Instant.ofEpochMilli(date))
        return c.format(withTime)
    }

    fun getPeriod(): String {
        val difference: Period = dateDiff();
        val per = "${difference.years}Y, ${difference.months}M, ${difference.days}D"
        if (withTime) {
            val diff = milliDiff() / 1000
            val h = (diff / 3600 % 24).toString()
            val m = (diff / 60 % 60).toString()
            return "$per  -  ${h}H, ${m}M"
        }
        return per
    }
}
