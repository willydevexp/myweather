package com.example.myweather.model

import android.location.Location
import android.provider.Settings.Global.getString
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.myweather.App
import com.example.myweather.R
import com.example.myweather.model.database.Weather
import com.example.myweather.model.datasource.WeatherLocalDataSource
import com.example.myweather.model.datasource.WeatherRemoteDataSource
import com.example.myweather.model.location.LocationRepository
import com.example.myweather.model.remote.DayWeather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class WeatherRepository (application: App) {

    private val locationRepository =  LocationRepository(application)
    private val localDataSource = WeatherLocalDataSource(application.db.weatherDao())
    private val remoteDataSource = WeatherRemoteDataSource(
        application.getString(R.string.api_key)
    )

    val weatherList = localDataSource.weatherList

    suspend fun requestWeatherList(forceRefresh: Boolean = false)  {
        if (localDataSource.isEmpty() || forceRefresh) {
            locationRepository.findLastLocation()?.let {
                //Log.i("WeatherRepository", "Location LastCity: ${locationRepository.getLastCity()}")
                val dailyWeather = remoteDataSource.getDailyWeather(it)
                localDataSource.updateWeatherList(dailyWeather.list.toLocalModel())
                //Log.i("WeatherRepository", "Weather City: ${dailyWeather.city.name}")
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



