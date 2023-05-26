package com.example.myweather.ui.weather


import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.myweather.R
import com.example.myweather.databinding.FragmentWeatherBinding
import com.example.myweather.domain.Error
import com.example.myweather.ui.common.launchAndCollect
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherFragment : Fragment(R.layout.fragment_weather) {

    private val safeArgs: WeatherFragmentArgs by navArgs()

    private lateinit var weatherState: MainState

    private val viewModel: WeatherViewModel by viewModels ()

    private val adapter = WeatherListAdapter { weatherState.onWeatherClicked(it)}


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        weatherState = buildMainState()

        val binding = FragmentWeatherBinding.bind(view).apply {
            toolbar.setUp()

            swiperefresh.setOnRefreshListener {
                viewModel.refresh()
            }

            rvWeather.adapter = adapter
        }

        viewLifecycleOwner.launchAndCollect(viewModel.state) {
            binding.updateUI(it)
        }

        //viewModel.getWeather()

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

    private fun FragmentWeatherBinding.updateUI(state: WeatherViewModel.UiState) {
        val appName = getString(R.string.app_name)
        toolbar.title = "$appName - ${state.locationName}"

        swiperefresh.isRefreshing = state.isRefreshing

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
