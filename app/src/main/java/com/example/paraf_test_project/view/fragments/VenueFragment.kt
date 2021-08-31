package com.example.paraf_test_project.view.fragments

import android.app.Activity
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
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
import androidx.core.content.ContextCompat.getSystemService
import java.io.IOException


class VenueFragment : Fragment(), LocationListener {

    private lateinit var venueViewModel: VenueViewModel
    private lateinit var userViewModel: UserViewModel
    private var mAdapter = VenueAdapter(arrayListOf())
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private lateinit var binding: VenueFragmentBinding
    private val locationPermissionCode = 2
    private lateinit var locationService: LocationService;
    private var coordination: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.venue_fragment, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        locationService = LocationService(context as Activity)
        venueViewModel = ViewModelProvider(this)[VenueViewModel::class.java]
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        locationService.getLocation(this)

        mLayoutManager = LinearLayoutManager(context)

        recycler_view.apply {
            adapter = mAdapter
            layoutManager = mLayoutManager
        }
        observerViewModel()
    }

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

    override fun onLocationChanged(location: Location) {
        val ll = "${location.latitude},${location.longitude}"
        venueViewModel.fetch(
            location.latitude,
            location.longitude,
        )
        userViewModel.getAddress(location.latitude, location.longitude)

        Log.d("tag provider", location.provider)
    }
}