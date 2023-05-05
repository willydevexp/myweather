package com.example.myweather.data.datasource

import arrow.core.Either
import com.example.myweather.domain.DomainLocation
import com.example.myweather.domain.Error
import com.example.myweather.domain.Weather


interface WeatherRemoteDataSource {
    suspend fun getDailyWeather(location: DomainLocation): Either<Error, List<Weather>>
}

