package io.github.husseinfo.countin

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import io.github.husseinfo.countin.data.AppDatabase
import io.github.husseinfo.countin.data.CountModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class AddItemActivity : AppCompatActivity() {
    private var date: Long = 0L
    private lateinit var tvDate: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        tvDate = findViewById(R.id.tv_date)
        Calendar.getInstance().also {
            tvDate.text = it.printFormatted()
            date = it.time.time
        }

        tvDate.setOnClickListener {
            val c = Calendar.getInstance()
            c.time = Date.from(Instant.ofEpochMilli(date))
            val currentYear = c[Calendar.YEAR]
            val currentMonth = c[Calendar.MONTH]
            val currentDay = c[Calendar.DAY_OF_MONTH]

            DatePickerDialog(this).also {
                it.datePicker.init(
                    currentYear,
                    currentMonth,
                    currentDay
                ) { _: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                    val calendar = Calendar.getInstance()
                    calendar.set(year, monthOfYear, dayOfMonth)
                    date = calendar.time.time
                    tvDate.text = calendar.printFormatted()
                }
                it.datePicker.maxDate = Calendar.getInstance().time.time
                it.show()
            }
        }

        findViewById<Button>(R.id.btn_dismiss).setOnClickListener { finish() }

        findViewById<Button>(R.id.btn_save).setOnClickListener {
            val title = (findViewById<View>(R.id.et_new_item_title) as EditText).text.toString()
            if (title.isEmpty()) {
                Snackbar.make(
                    findViewById(R.id.layout),
                    R.string.error_empty_title,
                    BaseTransientBottomBar.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            val model = CountModel(title, date, false)
            MainScope().launch(Dispatchers.IO) {
                AppDatabase.getDb(baseContext)!!.countDAO()!!.insertAll(model)
            }
            finish()
        }
    }
}

fun Calendar.printFormatted(): CharSequence {
    return DateTimeFormatter.ofPattern("dd/MM/yyyy")
        .format(ZonedDateTime.ofInstant(toInstant(), ZoneId.systemDefault()))
}
