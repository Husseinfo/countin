package io.github.husseinfo.countin.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "count")
class CountModel(
    @field:ColumnInfo(name = "title") var title: String, @field:ColumnInfo(
        name = "date"
    ) var date: Long
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id = 0

    val years: Long
        get() = getDiff() / 1000 / 60 / 60 / 24 / 30 / 12

    val months: Long
        get() = (getDiff() / 1000 / 60 / 60 / 24 / 30) % 12

    val days: Long
        get() = (getDiff() / 1000 / 60 / 60 / 24) % 30


    override fun toString(): String {
        return "$date;$title"
    }

    private fun getDiff(): Long {
        return Calendar.getInstance().time.time - date
    }
}
