package com.example.myweather.data.datasource

import com.example.myweather.domain.DomainLocation
import com.example.myweather.domain.Weather
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    val weatherList: Flow<List<Weather>>

    val locationList: Flow<List<DomainLocation>>


    fun getWeather (dt: Int) : Flow<Weather>

    fun getWeatherOfLocation (locationId: Int) : Flow<List<Weather>>

    suspend fun isWeatherListEmpty(): Boolean

    suspend fun insertWeatherList(locationId: Int, newWeatherList: List<Weather>)

    suspend fun getLocation (locationId: Int) : DomainLocation

    suspend fun isLocationListEmpty(): Boolean

    suspend fun addLocation(location: DomainLocation)

    suspend fun delLocation(locationId: Int)
}

