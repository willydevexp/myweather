package com.example.myweather.model

import android.util.Log
import com.example.myweather.App
import com.example.myweather.R
import com.example.myweather.common.Error
import com.example.myweather.common.tryCall
import com.example.myweather.model.database.Weather
import com.example.myweather.model.datasource.WeatherLocalDataSource
import com.example.myweather.model.datasource.WeatherRemoteDataSource
import com.example.myweather.model.location.LocationRepository
import com.example.myweather.model.remote.DayWeather
import kotlinx.coroutines.flow.Flow

class WeatherRepository (application: App) {

    private val locationRepository =  LocationRepository(application)
    private val localDataSource = WeatherLocalDataSource(application.db.weatherDao())
    private val remoteDataSource = WeatherRemoteDataSource(
        application.getString(R.string.api_key)
    )

    val weatherList = localDataSource.weatherList

    suspend fun requestWeatherList(forceRefresh: Boolean = false): Error? = tryCall {
        if (localDataSource.isEmpty() || forceRefresh) {
            locationRepository.findLastLocation()?.let {
                val dailyWeather = remoteDataSource.getDailyWeather(it)
                localDataSource.updateWeatherList(dailyWeather.list.toLocalModel())
            }
        }
    }

    fun getWeather (dt: Int) : Flow<Weather> = localDataSource.getWeather(dt)

    suspend fun getCityName () : String = locationRepository.getLastCity()
}

private fun List<DayWeather>.toLocalModel(): List<Weather> = map { it.toLocalModel() }

private fun DayWeather.toLocalModel(): Weather = Weather(
    dt,
    temp.max,
    temp.min,
    humidity,
    pressure,
    speed,
    weather[0].description,
    weather[0].icon
)



