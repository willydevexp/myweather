package com.example.myweather.ui.main


import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.myweather.R
import com.example.myweather.ui.common.app
import com.example.myweather.data.WeatherRepository
import com.example.myweather.data.LocationRepository
import com.example.myweather.databinding.FragmentMainBinding
import com.example.myweather.framework.AndroidPermissionChecker
import com.example.myweather.framework.PlayServicesLocationDataSource
import com.example.myweather.framework.database.WeatherRoomDataSource
import com.example.myweather.framework.server.WeatherServerDataSource
import com.example.myweather.usecases.GetCityNameUseCase
import com.example.myweather.usecases.GetWeatherListUseCase
import com.example.myweather.usecases.RequestWeatherListUseCase
import com.example.myweather.ui.common.launchAndCollect
import com.google.android.material.snackbar.Snackbar
import com.example.myweather.domain.Error


class MainFragment : Fragment(R.layout.fragment_main) {

    private lateinit var mainState: MainState

    private val viewModel: MainViewModel by viewModels {
        val application = requireActivity().app

        val locationRepository =  LocationRepository(
            PlayServicesLocationDataSource(application),
            AndroidPermissionChecker(application)
        )
        val localDataSource = WeatherRoomDataSource(requireActivity().app.db.weatherDao())
        val remoteDataSource = WeatherServerDataSource(
            getString(R.string.api_key)
        )
        val repository = WeatherRepository(locationRepository, localDataSource, remoteDataSource)

        MainViewModelFactory(
            GetCityNameUseCase(repository),
            GetWeatherListUseCase(repository),
            RequestWeatherListUseCase(repository)
        )
    }

    private val adapter = WeatherListAdapter { mainState.onWeatherClicked(viewModel.state.value.cityName, it)}


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainState = buildMainState()

        val binding = FragmentMainBinding.bind(view).apply {
            toolbar.setUp()

            swiperefresh.setOnRefreshListener {
                viewModel.refresh()
            }

            rvWeather.adapter = adapter
        }

        viewLifecycleOwner.launchAndCollect(viewModel.state) {
            binding.updateUI(it)
        }

        mainState.requestLocationPermission {
            viewModel.refresh()
        }
    }

    private fun Toolbar.setUp () {
        inflateMenu(R.menu.main_menu)
        setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_refresh -> {
                    viewModel.refresh()
                    true
                }
                else -> {false}
            }
        }
    }

    private fun FragmentMainBinding.updateUI(state: MainViewModel.UiState) {
        Log.i("MainActivity.updateUI", "isRefreshing: ${state.isRefreshing}. CityName: ${state.cityName}. WeatherList: ${state.weatherList}")
        swiperefresh.isRefreshing = state.isRefreshing
        if (state.cityName!="") {
            val appName = getString(R.string.app_name)
            toolbar.title = "$appName - ${state.cityName}"
        }
        state.weatherList?.let(adapter::submitList)
        if (state.error!=null)
            Snackbar.make(myCoordinatorLayout, errorToString(state.error), Snackbar.LENGTH_LONG).show()
    }

    fun errorToString(error: Error) = when (error) {
        Error.Connectivity -> getString(R.string.connectivity_error)
        is Error.Server -> getString(R.string.server_error) + error.code
        is Error.Unknown -> getString(R.string.unknown_error) + error.message
    }

}
