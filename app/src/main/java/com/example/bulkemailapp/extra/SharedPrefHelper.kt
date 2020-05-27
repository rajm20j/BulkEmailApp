package com.example.bulkemailapp.extra

import android.content.Context
import android.content.SharedPreferences

class SharedPrefHelper(context: Context) {
    init {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    companion object {
        private const val PREFS_NAME = "appname_prefs"
        private lateinit var sharedPreferences: SharedPreferences

        fun setInt(key: String, value: Int) {
            sharedPreferences.edit().putInt(key, value)?.apply()
        }

        fun getInt(key: String): Int {
            return sharedPreferences.getInt(key, 0)
        }

        fun setStr(key: String, value: String) {
            sharedPreferences.edit().putString(key, value).apply()
        }

        fun getStr(key: String): String? {
            if(sharedPreferences.contains(key))
                return sharedPreferences.getString(key, "NULL")
            else
                return "NULL"
        }

        fun clrStr() {
            sharedPreferences.edit().clear().apply()
        }

        fun setBool(key: String, value: Boolean) {
            sharedPreferences.edit().putBoolean(key, value).apply()
        }

        fun getBool(key: String): Boolean {
            return sharedPreferences.getBoolean(key, false)
        }
    }
}
