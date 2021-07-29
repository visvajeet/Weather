package com.skf.weatherapp.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit


object SharedPref {


    private lateinit var pref: SharedPreferences

    fun data(context: Context) {
        val appName = "weatherApp"
        pref = context.applicationContext.getSharedPreferences(
            appName,
            Context.MODE_PRIVATE
        )!!

    }
    fun setBoolean(key: String, value: Boolean) {

        pref.edit {
            putBoolean(key, value)
        }
    }

    fun setString(key: String, value: String) {

        pref.edit {
            putString(key, value)
        }

    }

    fun getBoolean(key: String): Boolean {

        return pref.getBoolean(key, false)

    }

    fun getString(key: String): String {

        return pref.getString(key, "")!!

    }


    fun setInt(key: String, value: Int) {

        pref.edit {
            putInt(key, value)
        }

    }


    fun getInt(key: String): Int {

        return pref.getInt(key, 0)

    }

    fun setLong(key: String, value: Long) {

        pref.edit {
            putLong(key, value)
        }

    }


    fun getLong(key: String): Long {

        return pref.getLong(key, 0)

    }


}