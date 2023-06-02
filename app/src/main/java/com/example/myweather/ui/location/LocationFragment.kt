package com.example.myweather.ui.location

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.myweather.R
import com.example.myweather.databinding.FragmentLocationBinding
import com.example.myweather.domain.Error
import com.example.myweather.ui.common.launchAndCollect
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationFragment : Fragment(R.layout.fragment_location) {

    private lateinit var locationsState: LocationState

    private val viewModel: LocationViewModel by viewModels ()

    private val adapter = LocationListAdapter (
        clickListener = { locationsState.onLocationClicked(it.id) },
        delListener = { viewModel.delLocation(it.id) }
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Listener para el resultado del DialogFragment
        setFragmentResultListener(LocationDialog.requestKey) { key, bundle ->
            val locationName = bundle.getString("locationName")
            locationName?.let { viewModel.addLocation(locationName) }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        locationsState = buildLocationsState()

        val binding = FragmentLocationBinding.bind(view).apply {
            cardlastLocation.setOnClickListener {
                locationsState.onLocationClicked(-1)
            }
            rvLocations.adapter = adapter
            fab.setOnClickListener { showLocationDialog() }
        }

        viewLifecycleOwner.launchAndCollect(viewModel.state) {
            binding.updateUI(it)
        }

        locationsState.requestLocationPermission {
            viewModel.getLastLocation()
        }

    }

    private fun FragmentLocationBinding.updateUI(state: LocationViewModel.UiState) {
        state.lastLocation?.let {location ->
            txtLastLocation.text = "${location.name} - ${location.countryCode}"
        }
        state.locationList?.let(adapter::submitList)
        if (state.error!=null)
            Snackbar.make(myCoordinatorLayout, errorToString(state.error), Snackbar.LENGTH_LONG).show()
    }


    private fun showLocationDialog() {
        findNavController().navigate(R.id.action_locationFragment_to_locationDialog)
    }


    fun errorToString(error: Error) = when (error) {
        Error.Connectivity -> getString(R.string.connectivity_error)
        is Error.Server -> getString(R.string.server_error) + error.code
        is Error.Unknown -> getString(R.string.unknown_error) + error.message
    }


}

