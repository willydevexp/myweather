package com.example.myweather.model.datasource

import android.location.Location
import com.example.myweather.model.remote.RemoteConnection

class WeatherRemoteDataSource(private val apiKey: String) {

    suspend fun getDailyWeather(location: Location) =
        RemoteConnection.service
            .getDailyWeather(
                location.latitude,
                location.longitude,
                apiKey
            )
}