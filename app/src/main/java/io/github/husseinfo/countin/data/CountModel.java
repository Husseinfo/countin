package io.github.husseinfo.countin.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "count")
public class CountModel {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "date")
    public String date;

    public CountModel(String title, String date) {
        this.title = title;
        this.date = date;
    }

    public String getYears() {
        return "1";
    }

    public String getMonths() {
        return "2";
    }

    public String getDays() {
        return "3";
    }

    @NonNull
    @Override
    public String toString() {
        return this.date + ';' + this.title;
    }
}
