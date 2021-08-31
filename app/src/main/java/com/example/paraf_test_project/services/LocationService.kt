package com.example.paraf_test_project.services

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationListener
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*

class LocationService(val context: Context) {
    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2
    private val geocoder: Geocoder = Geocoder(context, Locale.getDefault())

    fun getLocation(listener: LocationListener) {
        locationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                locationPermissionCode
            )
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 200f, listener)

    }

    fun getAddress(latitude: Double, longitude: Double): String {
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
        return addresses[0].getAddressLine(0)
    }
}

