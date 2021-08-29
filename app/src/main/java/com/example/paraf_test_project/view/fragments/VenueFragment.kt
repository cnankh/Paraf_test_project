package com.example.paraf_test_project.view.fragments

import android.app.Activity
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.paraf_test_project.services.LocationService
import com.example.paraf_test_project.R
import com.example.paraf_test_project.viewmodel.VenueViewModel

class VenueFragment : Fragment(), LocationListener {

    private lateinit var viewModel: VenueViewModel
    private val locationPermissionCode = 2
    private lateinit var locationService: LocationService;
    private var coordination: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.venue_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        locationService = LocationService(context as Activity)
        viewModel = ViewModelProvider(this)[VenueViewModel::class.java]

        locationService.getLocation(this)
    }

    override fun onLocationChanged(location: Location) {
        val ll = "${location.latitude},${location.longitude}"
        viewModel.fetch(ll)
    }

}