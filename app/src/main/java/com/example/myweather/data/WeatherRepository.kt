package com.example.myweather.data

import com.example.myweather.App
import com.example.myweather.R
import com.example.myweather.ui.common.Error
import com.example.myweather.ui.common.tryCall
import com.example.myweather.data.database.Weather
import com.example.myweather.data.datasource.WeatherLocalDataSource
import com.example.myweather.data.datasource.WeatherRemoteDataSource
import com.example.myweather.data.location.LocationRepository
import com.example.myweather.data.remote.DayWeather
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



