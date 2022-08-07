package io.github.husseinfo.countin;

import androidx.annotation.NonNull;

public class CountModel {

    private String title;
    private String date;

    public CountModel(String title, String date) {
        this.title = title;
        this.date = date;
    }

    @NonNull
    @Override
    public String toString() {
        return this.date + ';' + this.title;
    }
}
