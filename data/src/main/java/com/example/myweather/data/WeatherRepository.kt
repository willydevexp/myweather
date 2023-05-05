package com.example.myweather.data

import com.example.myweather.data.datasource.WeatherLocalDataSource
import com.example.myweather.data.datasource.WeatherRemoteDataSource
import com.example.myweather.domain.Weather
import com.example.myweather.domain.Error
import kotlinx.coroutines.flow.Flow


class WeatherRepository (
    private val locationRepository: LocationRepository,
    private val localDataSource: WeatherLocalDataSource,
    private val remoteDataSource: WeatherRemoteDataSource) {

    val weatherList = localDataSource.weatherList

    suspend fun requestWeatherList(forceRefresh: Boolean = false): Error? {
        if (localDataSource.isEmpty() || forceRefresh) {
            val dailyWeather = remoteDataSource.getDailyWeather(locationRepository.findLastLocation())
            dailyWeather.fold(ifLeft = { return it }) {
                localDataSource.updateWeatherList(it)
            }
        }
        return null
    }

    suspend fun getCityName(): String = locationRepository.findLastLocation().name

    fun getWeather(dt: Int): Flow<Weather> = localDataSource.getWeather(dt)

}




