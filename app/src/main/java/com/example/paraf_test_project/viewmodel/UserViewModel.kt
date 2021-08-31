package com.example.paraf_test_project.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.location.Geocoder
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.paraf_test_project.model.User
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*

class UserViewModel(application: Application) : BaseViewModel(application) {
    private final val TAG = "tag-userViewModel: "

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext
    private lateinit var geocoder: Geocoder
    val user = MutableLiveData<User>()

    /**
     * based on user latitude and longitude , get the address
     */
    fun getAddress(latitude: Double, longitude: Double) {

        launch {
            geocoder = Geocoder(context, Locale.getDefault())

            try {
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                val address = addresses[0].getAddressLine(0)

                val _user = User(address)

                user.value = _user
            } catch (e: IOException) {
                Log.e(TAG, e.message.toString())
            }


        }
    }

}