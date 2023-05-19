package com.example.myweather.ui.weather

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.myweather.domain.Weather

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
