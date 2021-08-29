package com.example.paraf_test_project.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.paraf_test_project.R
import com.example.paraf_test_project.viewmodel.VenueDetailsViewModel

class VenueDetailsFragment : Fragment() {

    private lateinit var viewModel: VenueDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.venue_details_fragment, container, false)
    }

}