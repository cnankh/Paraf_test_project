package com.example.paraf_test_project.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager

class SharedPreferencesHelper {

    private val LATITUDE = "latitude"
    private val LONGITUDE = "longitude"

    companion object {

        private var prefs: SharedPreferences? = null

        private var instance: SharedPreferencesHelper? = null
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

    /**
     * store latitude
     * @param latitude : Float
     */
    fun setLatitude(latitude: Float) {
        prefs?.edit(commit = true) {
            putFloat(LATITUDE, latitude)
        }
    }

    /**
     * get stored latitude
     * @return float
     */
    fun getPreviousLatitude(): Float? {
        return prefs?.getFloat(LATITUDE, 0.0f)
    }

    /**
     * store longitude
     * @param longitude : Float
     */
    fun setLongitude(longitude: Float) {
        prefs?.edit(commit = true) {
            putFloat(LONGITUDE, longitude)
        }
    }

    /**
     * get stored longitude
     * @return float
     */
    fun getPreviousLongitude(): Float? {
        return prefs?.getFloat(LONGITUDE, 0.0F)
    }

}