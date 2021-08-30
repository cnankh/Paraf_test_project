package com.example.paraf_test_project.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.paraf_test_project.R
import com.example.paraf_test_project.databinding.VenueItemBinding
import com.example.paraf_test_project.model.Venue
import com.example.paraf_test_project.view.fragments.VenueFragmentDirections
import com.example.paraf_test_project.view.interfaces.VenueClickListener
import kotlinx.android.synthetic.main.venue_item.view.*

class VenueAdapter(val list: ArrayList<Venue>) :
    RecyclerView.Adapter<VenueAdapter.CustomViewHolder>(), VenueClickListener {

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<Venue>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()

    }

    class CustomViewHolder(var view: VenueItemBinding) : RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view =
            DataBindingUtil.inflate<VenueItemBinding>(inflater, R.layout.venue_item, parent, false)

        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.view.venue = list[position]
        holder.view.listener = this

    }

    override fun getItemCount() = list.size

    override fun onCLick(view: View) {
        val uuid = view.uuid_container.text.toString().toInt()
        val action = VenueFragmentDirections.actionVenueFragmentToVenueDetailsFragment()
        action.uuid = uuid
        Navigation.findNavController(view).navigate(action)
    }

}