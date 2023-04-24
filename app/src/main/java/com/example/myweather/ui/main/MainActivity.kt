package com.example.myweather.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.myweather.R
import com.example.myweather.common.Error
import com.example.myweather.common.app
import com.example.myweather.databinding.ActivityMainBinding
import com.example.myweather.model.WeatherRepository
import com.example.myweather.model.database.Weather
import com.example.myweather.ui.detail.DetailActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels { MainViewModelFactory(WeatherRepository(app)) }
    private val adapter = WeatherListAdapter { viewModel.onWeatherClicked(it) }
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvWeather.adapter = adapter

        // Actualizamos la UI cuando actualice el viewmodel
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect(::updateUI)
            }
        }

        binding.swiperefresh.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    private fun updateUI(state: MainViewModel.UiState) {
        Log.i("MainActivity.updateUI", "isRefreshing: ${state.isRefreshing}. CityName: ${state.cityName}. WeatherList: ${state.weatherList}")
        binding.swiperefresh.isRefreshing = state.isRefreshing
        if (state.cityName!="") {
            val app_name = getString(R.string.app_name)
            supportActionBar?.title = "$app_name - ${state.cityName}"
        }
        state.weatherList?.let(adapter::submitList)
        state.navigateTo?.let(::navigateTo)
        binding.error.text = state.error?.let(::errorToString)
    }

    private fun navigateTo(weather: Weather) {
        Log.i ("MainActiviy.navigateTo", "CityName: ${viewModel.state.value.cityName}. Weather: $weather")
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.CITY_NAME, viewModel.state.value.cityName)
        intent.putExtra(DetailActivity.WEATHER_DT, weather.dt)
        startActivity(intent)
        viewModel.onNavigateDone()
    }

    fun errorToString(error: Error) = when (error) {
        Error.Connectivity -> getString(R.string.connectivity_error)
        is Error.Server -> getString(R.string.server_error) + error.code
        is Error.Unknown -> getString(R.string.unknown_error) + error.message
    }

    /** Create main options menu */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    /** Listen for option item selections so that we receive a notification
     * when the user requests a refresh by selecting the refresh action bar item.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_refresh -> {
                viewModel.refresh()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


}
