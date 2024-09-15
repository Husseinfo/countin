package io.github.husseinfo.countin

import android.content.Context
import androidx.preference.PreferenceManager

const val APP_INTRO_FLAG = "APP_INTRO_FLAG"
const val APP_AUTH = "APP_AUTH"

fun saveFirstRun(context: Context) {
    val sharedPref = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
    val editor = sharedPref.edit()
    editor.putBoolean(APP_INTRO_FLAG, true)
    editor.apply()
}

fun isFirstRun(context: Context): Boolean {
    val sharedPref = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
    return sharedPref.getBoolean(APP_INTRO_FLAG, false)
}
