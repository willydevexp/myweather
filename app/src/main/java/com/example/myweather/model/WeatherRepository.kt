package com.example.myweather.model

import androidx.appcompat.app.AppCompatActivity
import com.example.myweather.R

class WeatherRepository (activity: AppCompatActivity) {

    private val apiKey = activity.getString(R.string.api_key)
    private val locationRepository = LocationRepository(activity)

    suspend fun getDailyWeather() =
        locationRepository.getLastLocation()?.let {
            RemoteConnection.service
                .getDailyWeather(
                    it.latitude,
                    it.longitude,
                    apiKey
                )
        }

}