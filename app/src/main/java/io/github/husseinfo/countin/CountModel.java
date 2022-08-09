package io.github.husseinfo.countin;

import androidx.annotation.NonNull;

public class CountModel {

    private String title;
    private String date;

    public CountModel() {
    }

    public CountModel(String title, String date) {
        this.title = title;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public static CountModel load(String s) {
        return new CountModel(s.split(";")[1], s.split(";")[0]);
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
