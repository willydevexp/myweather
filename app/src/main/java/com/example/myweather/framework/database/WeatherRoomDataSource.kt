package com.example.myweather.framework.database

import com.example.myweather.data.datasource.WeatherLocalDataSource
import kotlinx.coroutines.flow.map

class WeatherRoomDataSource(private val weatherDao: WeatherDao) : WeatherLocalDataSource {

    override val weatherList = weatherDao.getAll().map { it.toDomainModel() }

    override fun getWeather (dt: Int) = weatherDao.getWeather(dt).map { it.toDomainModel() }

    override suspend fun isEmpty(): Boolean = weatherDao.weatherCount() == 0

    override suspend fun updateWeatherList(newWeatherList: List<com.example.myweather.domain.Weather>) {
        weatherDao.deleteAll()
        weatherDao.insertWeatherList(newWeatherList.fromDomainModel())
    }
}

private fun List<WeatherTable>.toDomainModel(): List<com.example.myweather.domain.Weather> = map { it.toDomainModel() }

private fun WeatherTable.toDomainModel(): com.example.myweather.domain.Weather =
    com.example.myweather.domain.Weather(
        dt,
        tempMax,
        tempMin,
        humidity,
        pressure,
        speed,
        description,
        icon
    )

private fun List<com.example.myweather.domain.Weather>.fromDomainModel(): List<WeatherTable> =
    map { it.fromDomainModel() }

private fun com.example.myweather.domain.Weather.fromDomainModel(): WeatherTable = WeatherTable(
    dt,
    tempMax,
    tempMin,
    humidity,
    pressure,
    speed,
    description,
    icon
)
