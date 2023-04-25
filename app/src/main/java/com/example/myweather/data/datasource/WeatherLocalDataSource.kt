package com.example.myweather.data.datasource

import com.example.myweather.data.database.Weather
import com.example.myweather.data.database.WeatherDao
import kotlinx.coroutines.flow.Flow

class WeatherLocalDataSource(private val weatherDao: WeatherDao) {

    val weatherList: Flow<List<Weather>> = weatherDao.getAll()

    fun getWeather (dt: Int) : Flow<Weather> = weatherDao.getWeather(dt)

    suspend fun isEmpty(): Boolean = weatherDao.weatherCount() == 0

    suspend fun updateWeatherList(newWeatherList: List<Weather>) {
        weatherDao.deleteAll()
        weatherDao.insertWeatherList(newWeatherList)
    }
}