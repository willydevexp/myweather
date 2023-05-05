package com.example.myweather.data.datasource

import com.example.myweather.domain.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherLocalDataSource {
    val weatherList: Flow<List<Weather>>
    fun getWeather (dt: Int) : Flow<Weather>

    suspend fun isEmpty(): Boolean

    suspend fun updateWeatherList(newWeatherList: List<Weather>)
}

