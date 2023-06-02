package com.example.myweather.ui.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.myweather.R
import com.example.myweather.databinding.FragmentDetailBinding
import com.example.myweather.ui.common.getDate
import com.example.myweather.ui.common.loadUrl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.fragment_detail) {

    private val viewModel: DetailViewModel by viewModels ()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentDetailBinding.bind(view)
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { binding.updateUI(it) }
            }
        }
    }

    private fun FragmentDetailBinding.updateUI(state: DetailViewModel.UiState) {
        val appName = getString(R.string.app_name)
        toolbar.title = "$appName - ${state.locationName}"

        val weather = state.weather
        //Log.i ("MainActiviy.navigateTo", "CityName: ${state.cityName}. DayWeather: $dayWeather")
        weather?.let {
            weatherDetailImage.loadUrl("https://openweathermap.org/img/wn/${it.icon}@4x.png")
            weatherDetailSummary.text = getDate(it.dt)
            weatherDetailInfo.setWeatherInfo(it)
        }
    }

}
