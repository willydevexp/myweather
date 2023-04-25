package com.example.myweather.ui.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.example.myweather.R
import com.example.myweather.common.app
import com.example.myweather.common.getDate
import com.example.myweather.common.loadUrl
import com.example.myweather.data.WeatherRepository
import com.example.myweather.databinding.FragmentDetailBinding
import kotlinx.coroutines.launch

class DetailFragment : Fragment(R.layout.fragment_detail) {

    private val safeArgs: DetailFragmentArgs by navArgs()

    private val viewModel: DetailViewModel by viewModels {
        DetailViewModelFactory(requireNotNull(safeArgs.cityName),
            requireNotNull(safeArgs.weatherDT), WeatherRepository(requireActivity().app))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentDetailBinding.bind(view)
        binding.weatherDetailToolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { binding.updateUI(it) }
            }
        }
    }

    private fun FragmentDetailBinding.updateUI(state: DetailViewModel.UiState) {
        val weather = state.weather
        //Log.i ("MainActiviy.navigateTo", "CityName: ${state.cityName}. DayWeather: $dayWeather")
        //weatherDetailToolbar.title = state.cityName
        weather?.let {
            weatherDetailImage.loadUrl("https://openweathermap.org/img/wn/${it.icon}@4x.png")
            weatherDetailSummary.text = getDate(it.dt)
            weatherDetailInfo.setWeatherInfo(it)
        }
    }

}
