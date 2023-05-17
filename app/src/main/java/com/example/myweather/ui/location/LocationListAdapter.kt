package com.example.myweather.ui.location

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myweather.R
import com.example.myweather.databinding.ItemLocationBinding
import com.example.myweather.domain.DomainLocation
import com.example.myweather.ui.common.basicDiffUtil
import com.example.myweather.ui.common.inflate

class LocationListAdapter(private val clickListener: (DomainLocation) -> Unit,
                          private val delListener: (DomainLocation) -> Unit) :
    ListAdapter<DomainLocation,
            LocationListAdapter.ViewHolder>(basicDiffUtil { old, new -> old.id == new.id }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.inflate(R.layout.item_location, false)
        return ViewHolder(view, clickListener, delListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val location = getItem(position)
        holder.bind(location)
    }

    class ViewHolder(
        view: View,
        private val clickListener: (DomainLocation) -> Unit,
        private val delListener: (DomainLocation) -> Unit
    ) : RecyclerView.ViewHolder(view) {
        private val binding = ItemLocationBinding.bind(view)
        @SuppressLint("SetTextI18n")
        fun bind(location: DomainLocation) = with(binding) {
            root.setOnClickListener { clickListener(location) }
            txtLocation.text = "${location.name} - ${location.countryCode}"
            imgDelLocation.setOnClickListener{ delListener (location)}
        }
    }
}