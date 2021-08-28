package com.example.paraf_test_project.view.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.paraf_test_project.R
import com.example.paraf_test_project.viewmodel.LocationDetailsViewModel

class LocationDetailsFragment : Fragment() {

    private lateinit var viewModel: LocationDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.location_details_fragment, container, false)
    }

}