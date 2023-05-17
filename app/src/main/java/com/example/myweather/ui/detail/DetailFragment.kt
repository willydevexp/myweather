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
import com.example.myweather.ui.common.app
import com.example.myweather.ui.common.getDate
import com.example.myweather.ui.common.loadUrl
import com.example.myweather.data.AppRepository
import com.example.myweather.data.LocationServiceRepository
import com.example.myweather.databinding.FragmentDetailBinding
import com.example.myweather.framework.AndroidPermissionChecker
import com.example.myweather.framework.PlayServicesLocationServiceDataSource
import com.example.myweather.framework.database.RoomDataSource
import com.example.myweather.framework.server.WeatherServerDataSource
import com.example.myweather.usecases.weather.GetWeatherUseCase
import kotlinx.coroutines.launch

class DetailFragment : Fragment(R.layout.fragment_detail) {

    private val safeArgs: DetailFragmentArgs by navArgs()

    private val viewModel: DetailViewModel by viewModels {
        val application = requireActivity().app

        val localDataSource = RoomDataSource(requireActivity().app.db.appDao())
        val remoteDataSource = WeatherServerDataSource(
            getString(R.string.api_key)
        )

        val locationServiceRepository =  LocationServiceRepository(
            PlayServicesLocationServiceDataSource(application),
            AndroidPermissionChecker(application)
        )

        val repository = AppRepository(locationServiceRepository, localDataSource, remoteDataSource)

        DetailViewModelFactory(requireNotNull(safeArgs.weatherDT), GetWeatherUseCase(repository))
    }

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
        val weather = state.weather
        //Log.i ("MainActiviy.navigateTo", "CityName: ${state.cityName}. DayWeather: $dayWeather")
        weather?.let {
            weatherDetailImage.loadUrl("https://openweathermap.org/img/wn/${it.icon}@4x.png")
            weatherDetailSummary.text = getDate(it.dt)
            weatherDetailInfo.setWeatherInfo(it)
        }
    }

}
