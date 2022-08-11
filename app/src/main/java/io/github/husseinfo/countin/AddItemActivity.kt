package io.github.husseinfo.countin

import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import io.github.husseinfo.countin.data.AppDatabase
import io.github.husseinfo.countin.data.CountModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.util.*

class AddItemActivity : AppCompatActivity() {
    private var date: Long = 0L
    private lateinit var datePicker: DatePicker
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)
        datePicker = findViewById(R.id.dp_new_item_date)
        val c = Calendar.getInstance()
        date = c.time.time
        val currentYear = c[Calendar.YEAR]
        val currentMonth = c[Calendar.MONTH]
        val currentDay = c[Calendar.DAY_OF_MONTH]
        datePicker.init(
            currentYear,
            currentMonth,
            currentDay
        ) { _: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
            val calendar = Calendar.getInstance()
            calendar.set(year, monthOfYear, dayOfMonth)
            date = calendar.time.time
        }

        findViewById<View>(R.id.btn_dismiss).setOnClickListener { finish() }

        findViewById<View>(R.id.btn_save).setOnClickListener {
            val title = (findViewById<View>(R.id.et_new_item_title) as EditText).text.toString()
            if (title.isEmpty()) {
                Snackbar.make(
                    findViewById(R.id.layout),
                    R.string.error_empty_title,
                    BaseTransientBottomBar.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            val model = CountModel(title, date)
            MainScope().launch(Dispatchers.IO) {
                AppDatabase.getDb(baseContext)!!.countDAO()!!.insertAll(model)
            }
            finish()
        }
    }
}
