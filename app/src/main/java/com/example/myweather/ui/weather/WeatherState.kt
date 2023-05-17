package com.example.myweather.ui.weather

import android.Manifest
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.myweather.domain.Weather
import com.example.myweather.ui.common.PermissionRequester
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun Fragment.buildMainState(
    navController: NavController = findNavController(),
) = MainState(navController)

class MainState(
    private val navController: NavController
) {
    fun onWeatherClicked(weather: Weather) {
        val action = WeatherFragmentDirections.actionWeatherFragmentToDetailFragment(weather.dt)
        navController.navigate(action)
    }


}
