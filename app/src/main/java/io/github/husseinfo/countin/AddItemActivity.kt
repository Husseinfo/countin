package io.github.husseinfo.countin

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
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
    private var time: Int = 0
    private lateinit var tvDate: TextView
    private lateinit var swTime: SwitchCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        tvDate = findViewById(R.id.tv_date)
        swTime = findViewById(R.id.sw_time)
        Calendar.getInstance().also {
            tvDate.text = it.format()
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
                    calendar.set(year, monthOfYear, dayOfMonth, 0, 0)
                    date = calendar.time.time
                    tvDate.text = calendar.format()
                    swTime.isChecked = false
                }
                it.datePicker.maxDate = Calendar.getInstance().time.time
                it.show()
            }
        }

        swTime.setOnCheckedChangeListener { _, checked: Boolean ->
            if (!checked)
                return@setOnCheckedChangeListener

            val c = Calendar.getInstance().time
            TimePickerDialog(
                this,
                { _, hour, minute -> time = (hour * 3600 + minute * 60) * 1000 },
                c.hours,
                c.minutes,
                true
            ).show()
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

            if (swTime.isChecked)
                date += time
            val model = CountModel(title, date, swTime.isChecked)
            MainScope().launch(Dispatchers.IO) {
                AppDatabase.getDb(baseContext)!!.countDAO()!!.insertAll(model)
            }
            finish()
        }
    }
}

fun Calendar.format(): CharSequence {
    return DateTimeFormatter.ofPattern("dd/MM/yyyy")
        .format(ZonedDateTime.ofInstant(toInstant(), ZoneId.systemDefault()))
}
