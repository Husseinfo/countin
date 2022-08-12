package io.github.husseinfo.countin.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.*

@Entity(tableName = "count")
class CountModel(
    @field:ColumnInfo(name = "title") var title: String, @field:ColumnInfo(
        name = "date"
    ) var date: Long
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


    override fun toString(): String {
        return "$date;$title"
    }

    private fun getDiff(): Period {
        return Period.between(
            LocalDateTime.ofInstant(Instant.ofEpochMilli(date), ZoneOffset.UTC).toLocalDate(),
            LocalDate.now(ZoneOffset.UTC)
        )
    }

    fun formatDate(): String {
        val d = LocalDateTime.ofInstant(Instant.ofEpochMilli(date), ZoneOffset.UTC).toLocalDate()
        return d.toString()
    }
}
