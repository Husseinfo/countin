package io.github.husseinfo.countin.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.husseinfo.countin.activities.format
import java.time.*
import java.util.*

@Entity(tableName = "count")
class CountModel(
    @field:ColumnInfo(name = "title") var title: String,
    @field:ColumnInfo(name = "date") var date: Long,
    @field:ColumnInfo(name = "with_time") val withTime: Boolean
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

    fun milliDiff(): Long {
        return Calendar.getInstance().time.time - date
    }

    fun formatDate(): CharSequence {
        val c = Calendar.getInstance()
        c.time = Date.from(Instant.ofEpochMilli(date))
        return c.format(withTime)
    }
}
