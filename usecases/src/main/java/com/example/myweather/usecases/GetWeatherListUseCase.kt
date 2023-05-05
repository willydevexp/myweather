package com.example.myweather.usecases

import com.example.myweather.data.WeatherRepository
import com.example.myweather.domain.Weather
import kotlinx.coroutines.flow.Flow

class GetWeatherListUseCase(private val repository: WeatherRepository) {

    operator fun invoke(): Flow<List<Weather>> = repository.weatherList
}
