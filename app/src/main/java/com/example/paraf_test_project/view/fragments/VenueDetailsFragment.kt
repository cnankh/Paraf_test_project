package com.example.paraf_test_project.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.paraf_test_project.R
import com.example.paraf_test_project.databinding.VenueDetailsFragmentBinding
import com.example.paraf_test_project.viewmodel.VenueDetailsViewModel

class VenueDetailsFragment : Fragment() {

    private lateinit var viewModel: VenueDetailsViewModel
    private var uuid: Int = 0
    private lateinit var binding: VenueDetailsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.venue_details_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[VenueDetailsViewModel::class.java]
        arguments?.let {
            uuid = VenueDetailsFragmentArgs.fromBundle(it).uuid
        }
        viewModel.fetch(uuid)
        observer()

    }

    private fun observer() {
        viewModel.venueDetailLiveData.observe(viewLifecycleOwner, Observer {
            it?.let { venue ->
                binding.venue = venue
            }
        })
    }

}