package com.skf.weatherapp

import android.app.Application
import com.skf.weatherapp.utils.SharedPref
import timber.log.Timber

import timber.log.Timber.DebugTree

class AppClass : Application() {

    override fun onCreate() {
        super.onCreate()

        SharedPref.data(this)

        Timber.plant(DebugTree())
        if (BuildConfig.DEBUG) {

        }
    }

}