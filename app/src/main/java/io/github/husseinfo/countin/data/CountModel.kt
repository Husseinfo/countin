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

    val years: String
        get() {
            return "0"
        }
    val months: String
        get() = "2"
    val days: String
        get() = "3"

    override fun toString(): String {
        return "$date;$title"
    }

    fun getDiff() {
        val now = Calendar.getInstance().time
        val c = Calendar.getInstance()
//        c.set(date.split("_")[0], date.split("_")[1], date.split("_")[2])
    }
}
