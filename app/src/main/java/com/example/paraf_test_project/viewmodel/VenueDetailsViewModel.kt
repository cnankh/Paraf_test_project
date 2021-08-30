package com.example.paraf_test_project.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.paraf_test_project.model.Venue
import com.example.paraf_test_project.repository.VenueDatabase
import kotlinx.coroutines.launch

class VenueDetailsViewModel(application: Application) : BaseViewModel(application) {
    val venueDetailLiveData = MutableLiveData<Venue>()

    fun fetch(uuid: Int) {
        fetchFromDatabase(uuid)
    }

    private fun fetchFromDatabase(uuid: Int) {
        launch {
            val dao = VenueDatabase(getApplication()).venueDao()
            val result = dao.getVenue(uuid)
            venueDetailLiveData.value = result
        }
    }
}