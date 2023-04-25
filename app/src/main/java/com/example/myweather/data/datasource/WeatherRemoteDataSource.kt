package com.example.myweather.data.datasource

import android.location.Location
import com.example.myweather.data.remote.RemoteConnection

class WeatherRemoteDataSource(private val apiKey: String) {

    suspend fun getDailyWeather(location: Location) =
        RemoteConnection.service
            .getDailyWeather(
                location.latitude,
                location.longitude,
                apiKey
            )
}