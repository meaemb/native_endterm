package com.example.campuscompanion.util

import android.util.Log
import com.example.campuscompanion.BuildConfig

object AppLogger {

    fun d(tag: String, msg: String) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, msg)
        }
    }

    fun e(tag: String, msg: String, throwable: Throwable? = null) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, msg, throwable)
        }
    }
}
