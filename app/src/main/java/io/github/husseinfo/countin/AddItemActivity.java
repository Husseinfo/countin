package io.github.husseinfo.countin;

import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

public class AddItemActivity extends AppCompatActivity {

    private String date;
    private DatePicker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        datePicker = findViewById(R.id.dp_new_item_date);

        Calendar c = Calendar.getInstance();
        int currentYear = c.get(Calendar.YEAR);
        int currentMonth = c.get(Calendar.MONTH);
        int currentDay = c.get(Calendar.DAY_OF_MONTH);

        datePicker.init(currentYear, currentMonth, currentDay, (view, year, monthOfYear, dayOfMonth) -> this.date = year + "_" + monthOfYear + "_" + dayOfMonth);
        findViewById(R.id.btn_dismiss).setOnClickListener(v -> this.finish());
        findViewById(R.id.btn_save).setOnClickListener(v -> {
            String title = ((EditText) findViewById(R.id.et_new_item_title)).getText().toString();
            if (title.isEmpty()) {
                Snackbar.make(findViewById(R.id.layout), R.string.error_empty_title, BaseTransientBottomBar.LENGTH_SHORT).show();
                return;
            }
            CountModel model = new CountModel(title, date);
            Storage.saveItem(AddItemActivity.this, model, false);
            finish();
        });
    }
}
