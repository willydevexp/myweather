package com.example.myweather.ui.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.navigation.navArgs
import com.example.myweather.common.app
import com.example.myweather.common.getDate
import com.example.myweather.common.loadUrl
import com.example.myweather.databinding.ActivityDetailBinding
import com.example.myweather.model.WeatherRepository
import kotlinx.coroutines.launch
import java.util.*

class DetailActivity : AppCompatActivity() {
    companion object {
        const val CITY_NAME = "DetailActivity:cityName"
        const val WEATHER_DT = "DetailActivity:WeatherDT"
    }

    private val viewModel: DetailViewModel by viewModels {
        DetailViewModelFactory(requireNotNull(intent.getStringExtra(CITY_NAME)),
            intent.getIntExtra(WEATHER_DT, 0), WeatherRepository(app))
    }
    private lateinit var binding: ActivityDetailBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { updateUI(it) }
            }
        }
    }

    private fun updateUI(state: DetailViewModel.UiState) = with(binding) {
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
