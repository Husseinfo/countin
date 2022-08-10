package io.github.husseinfo.countin.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "count")
class CountModel(
    @field:ColumnInfo(name = "title") var title: String, @field:ColumnInfo(
        name = "date"
    ) var date: String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id = 0
    val years: String
        get() = "1"
    val months: String
        get() = "2"
    val days: String
        get() = "3"

    override fun toString(): String {
        return "$date;$title"
    }
}
