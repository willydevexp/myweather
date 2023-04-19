package com.example.myweather.model.datasource

import com.example.myweather.model.location.LocationRepository
import com.example.myweather.model.remote.RemoteConnection

class WeatherRemoteDataSource(private val apiKey: String, private val locationRepository: LocationRepository) {

    suspend fun getDailyWeather() =
        locationRepository.findLastLocation()?.let {
            RemoteConnection.service
                .getDailyWeather(
                    it.latitude,
                    it.longitude,
                    apiKey
                )
        }
}