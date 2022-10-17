package io.github.husseinfo.countin.activities

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.documentfile.provider.DocumentFile
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import io.github.husseinfo.countin.R
import io.github.husseinfo.countin.data.AppDatabase
import io.github.husseinfo.countin.data.CountModel
import io.github.husseinfo.maticonsearch.MaterialIconSelectorActivity
import io.github.husseinfo.maticonsearch.getIconByName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

const val EDIT_RECORD_ID = "EDIT_RECORD_ID"

class AddItemActivity : AppCompatActivity() {
    private var date: Long = 0L
    private var time: Int = 0
    private var icon: String? = null

    private lateinit var tvDate: MaterialTextView
    private lateinit var tvTime: MaterialTextView
    private lateinit var swTime: SwitchMaterial
    private lateinit var titleTextInput: TextInputEditText
    private var id: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        tvDate = findViewById(R.id.tv_date)
        tvTime = findViewById(R.id.tv_time)
        swTime = findViewById(R.id.sw_time)
        titleTextInput = findViewById(R.id.et_title)

        Calendar.getInstance().also {
            tvDate.text = it.format()
            it.set(Calendar.HOUR_OF_DAY, 0)
            it.set(Calendar.MINUTE, 0)
            it.set(Calendar.SECOND, 0)
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
                    calendar.set(year, monthOfYear, dayOfMonth, 0, 0, 0)
                    date = calendar.time.time
                    tvDate.text = calendar.format()
                    swTime.isChecked = false
                }
                it.datePicker.maxDate = Calendar.getInstance().time.time
                it.show()
            }
        }

        swTime.setOnCheckedChangeListener { _, checked: Boolean ->
            when {
                checked -> tvTime.visibility = View.VISIBLE
                else -> tvTime.visibility = View.GONE
            }
            if (!checked || time != 0) {
                time = 0
                return@setOnCheckedChangeListener
            }

            val c = Calendar.getInstance().time
            TimePickerDialog(
                this,
                { _, hour, minute ->
                    time = (hour * 3600 + minute * 60) * 1000
                    tvTime.text = "${hour.formatTime()}:${minute.formatTime()}"
                },
                c.hours,
                c.minutes,
                true
            ).show()
        }

        findViewById<MaterialButton>(R.id.btn_dismiss).setOnClickListener { finish() }

        findViewById<MaterialButton>(R.id.btn_save).setOnClickListener {
            val title = titleTextInput.text.toString()
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

            if (id > 0) {
                MainScope().launch(Dispatchers.IO) {
                    val model = CountModel(title, date, swTime.isChecked, icon)
                    model.id = id
                    AppDatabase.getDb(baseContext)!!.countDAO()!!.update(model)
                }
            } else {
                val model = CountModel(title, date, swTime.isChecked, icon)
                MainScope().launch(Dispatchers.IO) {
                    AppDatabase.getDb(baseContext)!!.countDAO()!!.insertAll(model)
                }
            }
            finish()
        }

        val iconView = findViewById<ComposeView>(R.id.icon)
        val resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val iconName = getIconByName(this, result)
                    icon = iconName.name
                    iconView.setContent {
                        Icon(
                            modifier = Modifier.size(40.dp),
                            imageVector = iconName,
                            contentDescription = iconName.name
                        )
                    }
                }
            }

        findViewById<MaterialButton>(R.id.btn_select_icon).setOnClickListener {
            resultLauncher.launch(Intent(this, MaterialIconSelectorActivity::class.java))
        }

        id = intent.getIntExtra(EDIT_RECORD_ID, -1)
        if (id > 0) {
            val modelCoroutine = MainScope().async(Dispatchers.IO) {
                AppDatabase.getDb(baseContext)?.countDAO()?.findById(id)
            }

            MainScope().launch(Dispatchers.Main) {
                val model = modelCoroutine.await()
                if (model != null) {
                    titleTextInput.setText(model.title)

                    val c = Calendar.getInstance()
                    c.time = Date.from(Instant.ofEpochMilli(model.date))
                    date = c.time.time
                    tvDate.text = c.format()
                    time = (c.get(Calendar.HOUR_OF_DAY) * 3600 + c.get(Calendar.MINUTE) * 60) * 1000

                    tvDate.text = c.format()
                    swTime.isChecked = model.withTime
                    tvTime.text = "${c.get(Calendar.HOUR_OF_DAY).formatTime()}:${
                        c.get(Calendar.MINUTE).formatTime()
                    }"
                    if (model.icon != null) {
                        icon = model.icon
                        iconView.setContent {
                            Icon(
                                modifier = Modifier.size(40.dp),
                                imageVector = getIconByName(baseContext, model.icon),
                                contentDescription = icon
                            )
                        }
                    }
                }
            }
        }

        if (intent?.action == Intent.ACTION_SEND) {
            try {
                val uri = intent.parcelable<Parcelable>(Intent.EXTRA_STREAM) as Uri

                val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(uri))
                findViewById<ImageView>(R.id.iv_background).also {
                    it.setImageBitmap(bitmap)
                    it.visibility = View.VISIBLE
                }

                DocumentFile.fromSingleUri(
                    this,
                    (intent.parcelable<Parcelable>(Intent.EXTRA_STREAM) as? Uri)!!
                )?.lastModified()!!.also {
                    val c = Calendar.getInstance()
                    c.time = Date.from(Instant.ofEpochMilli(it))
                    date = c.time.time
                    tvDate.text = c.format()
                    time = (c.get(Calendar.HOUR_OF_DAY) * 3600 + c.get(Calendar.MINUTE) * 60) * 1000
                    swTime.isChecked = true
                }
            } catch (e: Exception) {
                Toast.makeText(this, "Unable to fetch date! [$e]", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}

fun Calendar.format(time: Boolean = false): CharSequence {
    return DateTimeFormatter.ofPattern(("dd/MM/yyyy" + (if (time) " hh:mm" else "")))
        .format(ZonedDateTime.ofInstant(toInstant(), ZoneId.systemDefault()))
}

private inline fun <reified T> Intent.parcelable(key: String): T? =
    if (Build.VERSION.SDK_INT >= 33)
        getParcelableExtra(key, T::class.java)
    else
        @Suppress("DEPRECATION") getParcelableExtra(key) as? T


private fun Int.formatTime(): String {
    if (this < 10)
        return "0$this"
    return this.toString()
}