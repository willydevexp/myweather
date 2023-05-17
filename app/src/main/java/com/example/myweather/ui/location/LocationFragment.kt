package com.example.myweather.ui.location

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.myweather.R
import com.example.myweather.data.AppRepository
import com.example.myweather.data.LocationServiceRepository
import com.example.myweather.databinding.FragmentLocationBinding
import com.example.myweather.framework.AndroidPermissionChecker
import com.example.myweather.framework.PlayServicesLocationServiceDataSource
import com.example.myweather.framework.database.RoomDataSource
import com.example.myweather.framework.server.WeatherServerDataSource
import com.example.myweather.ui.common.app
import com.example.myweather.ui.common.launchAndCollect
import com.example.myweather.usecases.location.AddLocationUseCase
import com.example.myweather.usecases.location.DelLocationUseCase
import com.example.myweather.usecases.location.GetLastLocationUseCase
import com.example.myweather.usecases.location.GetLocationListUseCase

class LocationFragment : Fragment(R.layout.fragment_location) {

    private lateinit var locationsState: LocationState

    private val viewModel: LocationViewModel by viewModels {
        val application = requireActivity().app

        val localDataSource = RoomDataSource(requireActivity().app.db.appDao())

        val remoteDataSource = WeatherServerDataSource(
            getString(R.string.api_key)
        )

        val locationServiceRepository =  LocationServiceRepository(
            PlayServicesLocationServiceDataSource(application),
            AndroidPermissionChecker(application)
        )

        val appRepository = AppRepository(locationServiceRepository, localDataSource, remoteDataSource)

        LocationViewModelFactory(
            GetLastLocationUseCase(appRepository),
            GetLocationListUseCase(appRepository),
            AddLocationUseCase(appRepository),
            DelLocationUseCase(appRepository)
        )
    }

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
    }


    private fun showLocationDialog() {
        findNavController().navigate(R.id.action_locationFragment_to_locationDialog)
    }

}

