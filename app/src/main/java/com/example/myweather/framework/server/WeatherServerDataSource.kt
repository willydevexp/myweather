package com.example.myweather.framework.server

import arrow.core.Either
import com.example.myweather.data.datasource.WeatherRemoteDataSource
import com.example.myweather.di.ApiKey
import com.example.myweather.domain.DomainLocation
import com.example.myweather.domain.Error
import com.example.myweather.domain.Weather
import com.example.myweather.framework.tryCall
import javax.inject.Inject

class WeatherServerDataSource @Inject constructor (@ApiKey private val apiKey: String) : WeatherRemoteDataSource {

    override suspend fun getDailyWeather(location: DomainLocation): Either<Error, List<Weather>> =
        tryCall {
            RemoteConnection.service
                .getDailyWeather(
                    location.lat,
                    location.lon,
                    apiKey
                )
                .list
                .toDomainModel(location)
        }


    override suspend fun findLocation(locationName: String): Either<Error, DomainLocation> =
        tryCall {
            RemoteConnection.service
                .findLocation(
                    locationName,
                    apiKey
                )[0].toDomainModel()
        }

    private fun List<DayWeather>.toDomainModel(location: DomainLocation): List<Weather> =
        map { it.toDomainModel(location) }

    private fun DayWeather.toDomainModel(location: DomainLocation): Weather =
        Weather(
            dt,
            temp.max,
            temp.min,
            humidity,
            pressure,
            speed,
            weather[0].description,
            weather[0].icon,
            location.id
        )

    private fun FindLocationResponseItem.toDomainModel() : DomainLocation =
        DomainLocation(
            0,
            lat,
            lon,
            country,
            name
        )
}
