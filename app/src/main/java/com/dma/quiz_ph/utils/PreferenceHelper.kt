package com.dma.quiz_ph.utils

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import java.util.Locale

class PreferenceHelper {

    private val prefs: SharedPreferences? = null
    private val context: Context? = null

    companion object {
        private val configuration: Configuration? = null
        private val locale: Locale? = null
        private fun getPrefs(context: Context): SharedPreferences {
            return context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        }

        fun insertData(context: Context, key: String?, value: String?) {
            val editor = getPrefs(context).edit()
            editor.putString(key, value)
            editor.apply()
        }

        fun retriveData(context: Context, key: String?): String? {
            return getPrefs(context).getString(key, "")
        }

        fun deleteData(context: Context, key: String?) {
            val editor = getPrefs(context).edit()
            editor.remove(key)
            editor.apply()
        }
    }
}