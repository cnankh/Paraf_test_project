package com.example.paraf_test_project.view.fragments

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.paraf_test_project.services.LocationService
import com.example.paraf_test_project.R
import com.example.paraf_test_project.databinding.VenueFragmentBinding
import com.example.paraf_test_project.view.adapters.VenueAdapter
import com.example.paraf_test_project.viewmodel.UserViewModel
import com.example.paraf_test_project.viewmodel.VenueViewModel
import kotlinx.android.synthetic.main.venue_fragment.*
import androidx.fragment.app.FragmentActivity
import com.example.paraf_test_project.util.fragmentPermissionHelper.FragmentPermissionHelper
import com.example.paraf_test_project.util.fragmentPermissionHelper.FragmentPermissionInterface


class VenueFragment : Fragment(), LocationListener {

    private lateinit var venueViewModel: VenueViewModel
    private lateinit var userViewModel: UserViewModel
    private var mAdapter = VenueAdapter(arrayListOf())

    private lateinit var mLayoutManager: RecyclerView.LayoutManager

    private var _binding: VenueFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var locationService: LocationService;

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    private lateinit var fragmentActivity: FragmentActivity
    private lateinit var locationListener: LocationListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.venue_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        locationService = LocationService(context as Activity)
        venueViewModel = ViewModelProvider(this)[VenueViewModel::class.java]
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        locationListener = this

        checkPermission()

        mLayoutManager = LinearLayoutManager(context)

        configureViews()
        observerViewModel()
    }

    /**
     * observe live data
     */
    private fun observerViewModel() {
        venueViewModel.venuesList.observe(viewLifecycleOwner, Observer { venues ->
            venues?.let {
                mAdapter.updateList(venues)
                nothing_found.visibility = if (venues.isNullOrEmpty()) View.VISIBLE else View.GONE
            }

        })

        venueViewModel.loading.observe(viewLifecycleOwner, Observer {
            it?.let {
                progress_bar.visibility = if (it) View.VISIBLE else View.GONE
                recycler_view.visibility = if (it) View.GONE else View.VISIBLE
            }
        })

        userViewModel.user.observe(viewLifecycleOwner, Observer {
            it?.let { user ->
                binding.user = user
            }
        })


    }

    /**
     * this method gets called whenever location gets changed
     */
    override fun onLocationChanged(location: Location) {
        latitude = location.latitude
        longitude = location.longitude
        Log.d("tag venueFragment", "onLocation changed executed")
        venueViewModel.fetch(
            latitude,
            longitude,
        )
        userViewModel.getAddress(latitude, longitude)
    }

    /**
     * configure and manage views and widgets
     */
    private fun configureViews() {
        recycler_view.apply {
            adapter = mAdapter
            layoutManager = mLayoutManager
        }

        refreshLayout.setOnRefreshListener {

            val coordinates = "${latitude},${longitude}"
            venueViewModel.fetchFromRemote(coordinates)
            refreshLayout.isRefreshing = false


        }
    }

    /**
     * asks for needed permissions
     */
    private fun requestPermission() {
        FragmentPermissionHelper().startPermissionRequest(
            fragmentActivity,
            object : FragmentPermissionInterface {
                override fun onGranted(isGranted: Boolean) {
                    if (isGranted) {
                        locationService.getLocation(locationListener)
                    } else {

                        Toast.makeText(
                            context,
                            "this app needs location service permission",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }, android.Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    /**
     * check if the permission is not granted yet , ask for it
     */
    private fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(
                context as Activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermission()
        } else {
            locationService.getLocation(this)
        }
    }

    /**
     * this methods are needed to be override due to LocationListener functionality
     */

    override fun onProviderDisabled(provider: String) {}

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

    override fun onProviderEnabled(provider: String) {}


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentActivity = context as FragmentActivity
    }

}