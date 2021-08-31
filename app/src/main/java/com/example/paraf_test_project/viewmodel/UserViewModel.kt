package com.example.paraf_test_project.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.paraf_test_project.model.User
import com.example.paraf_test_project.services.LocationService
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*
import kotlin.concurrent.thread

class UserViewModel(application: Application) : BaseViewModel(application) {
    private final val TAG = "tag-userViewModel: "

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext
    private val location = LocationService(context)
    val user = MutableLiveData<User>()

    /**
     * based on user latitude and longitude , get the address
     */
    fun getAddress(latitude: Double, longitude: Double) {

        thread {
            try {
                val _user = User(location.getAddress(latitude, longitude))
                user.postValue(_user)
            } catch (e: IOException) {
                Log.e(TAG, e.message.toString())
            }

        }
    }

}