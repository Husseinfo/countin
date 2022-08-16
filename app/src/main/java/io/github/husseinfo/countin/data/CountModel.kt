package io.github.husseinfo.countin.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.husseinfo.countin.printFormatted
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

    val years: Int
        get() = getDiff().years

    val months: Int
        get() = getDiff().months

    val days: Int
        get() = getDiff().days

    private fun getDiff(): Period {
        return Period.between(
            LocalDateTime.ofInstant(Instant.ofEpochMilli(date), ZoneOffset.UTC).toLocalDate(),
            LocalDate.now(ZoneOffset.UTC)
        )
    }

    fun formatDate(): CharSequence {
        val c = Calendar.getInstance()
        c.time = Date.from(Instant.ofEpochMilli(date))
        return c.printFormatted()
    }
}

