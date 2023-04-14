package com.example.myweather.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.myweather.R
import com.example.myweather.common.visible
import com.example.myweather.databinding.ActivityMainBinding
import com.example.myweather.model.DayWeather
import com.example.myweather.model.WeatherRepository
import com.example.myweather.ui.detail.DetailActivity
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels { MainViewModelFactory(WeatherRepository(this)) }
    private val adapter = WeatherListAdapter { viewModel.onDayWeatherClicked(it) }
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvWeather.adapter = adapter

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.state.collect(::updateUI)
            }
        }

    }

    private fun updateUI(state: MainViewModel.UiState) {
        //Log.i("MainActivity.updateUI", "Progress: ${state.loading}. CityName: ${state.cityName}. WeatherList: ${state.weatherList}")
        binding.progress.visible = state.loading
        val app_name = getString(R.string.app_name)
        supportActionBar?.title = "$app_name - ${state.cityName}"
        state.weatherList?.let(adapter::submitList)
        state.navigateTo?.let(::navigateTo)
    }

    private fun navigateTo(dayWeather: DayWeather) {
        //Log.i ("MainActiviy.navigateTo", "CityName: ${viewModel.state.value.cityName}. DayWeather: $dayWeather")
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.CITY_NAME, viewModel.state.value.cityName)
        intent.putExtra(DetailActivity.DAY_WEATHER, dayWeather)
        startActivity(intent)
        viewModel.onNavigateDone()
    }

}
