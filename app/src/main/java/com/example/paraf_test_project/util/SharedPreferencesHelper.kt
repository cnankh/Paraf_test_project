package com.example.paraf_test_project.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager

class SharedPreferencesHelper {

    private final val LATITUDE = "latitude"
    private final val LONGITUDE = "longitude"

    companion object {

        private var prefs: SharedPreferences? = null

        var instance: SharedPreferencesHelper? = null
        private var LOCK = Any()

        operator fun invoke(context: Context): SharedPreferencesHelper =
            instance ?: synchronized(LOCK) {
                instance ?: buildHelper(context).also {
                    instance = it
                }
            }

        fun buildHelper(context: Context): SharedPreferencesHelper {
            prefs = PreferenceManager.getDefaultSharedPreferences(context)
            return SharedPreferencesHelper()
        }
    }

    fun setLatitude(latitude: Float) {
        prefs?.edit(commit = true) {
            putFloat(LATITUDE, latitude)
        }
    }

    fun getPreviousLatitude(): Float? {
        return prefs?.getFloat(LATITUDE, 0.0f)
    }

    fun setLongitude(longitude: Float) {
        prefs?.edit(commit = true) {
            putFloat(LONGITUDE, longitude)
        }
    }

    fun getPreviousLongitude(): Float? {
        return prefs?.getFloat(LONGITUDE, 0.0F)
    }

}