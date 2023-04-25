package com.example.myweather.ui.main


import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.myweather.R
import com.example.myweather.common.Error
import com.example.myweather.common.app
import com.example.myweather.data.WeatherRepository
import com.example.myweather.data.database.Weather
import com.example.myweather.databinding.FragmentMainBinding
import com.example.myweather.ui.detail.DetailFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class MainFragment : Fragment(R.layout.fragment_main) {

    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(WeatherRepository(requireActivity().app))
    }
    private val adapter = WeatherListAdapter { viewModel.onWeatherClicked(it) }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentMainBinding.bind(view).apply {
            rvWeather.adapter = adapter
            swiperefresh.setOnRefreshListener {
                viewModel.refresh()
            }
            toolbar.inflateMenu(R.menu.main_menu)
            toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_refresh -> {
                        viewModel.refresh()
                        true
                    }
                    else -> {false}
                }
            }
        }

        // Actualizamos la UI cuando actualice el viewmodel
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect {binding.updateUI(it)}
            }
        }

    }

    private fun FragmentMainBinding.updateUI(state: MainViewModel.UiState) {
        Log.i("MainActivity.updateUI", "isRefreshing: ${state.isRefreshing}. CityName: ${state.cityName}. WeatherList: ${state.weatherList}")
        swiperefresh.isRefreshing = state.isRefreshing
        if (state.cityName!="") {
            val app_name = getString(R.string.app_name)
            toolbar.title = "$app_name - ${state.cityName}"
        }
        state.weatherList?.let(adapter::submitList)
        state.navigateTo?.let(::navigateTo)
        if (state.error!=null)
            Snackbar.make(myCoordinatorLayout, errorToString(state.error), Snackbar.LENGTH_LONG).show()
    }

    private fun navigateTo(weather: Weather) {
        Log.i ("MainActiviy.navigateTo", "CityName: ${viewModel.state.value.cityName}. Weather: $weather")
        val navAction = MainFragmentDirections.actionMainFragmentToDetailFragment(
            viewModel.state.value.cityName, weather.dt
        )
        findNavController().navigate(navAction)
    }

    fun errorToString(error: Error) = when (error) {
        Error.Connectivity -> getString(R.string.connectivity_error)
        is Error.Server -> getString(R.string.server_error) + error.code
        is Error.Unknown -> getString(R.string.unknown_error) + error.message
    }

}
