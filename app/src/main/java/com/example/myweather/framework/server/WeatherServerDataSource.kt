package com.example.myweather.framework.server

import android.widget.RemoteViews.RemoteResponse
import arrow.core.Either
import com.example.myweather.data.datasource.WeatherRemoteDataSource
import com.example.myweather.domain.DomainLocation
import com.example.myweather.domain.Error
import com.example.myweather.framework.tryCall
import com.example.myweather.domain.Weather

class WeatherServerDataSource(private val apiKey: String) : WeatherRemoteDataSource {

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
}
