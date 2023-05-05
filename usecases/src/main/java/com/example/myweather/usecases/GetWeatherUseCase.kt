package com.example.myweather.usecases

import com.example.myweather.data.WeatherRepository
import com.example.myweather.domain.Weather
import kotlinx.coroutines.flow.Flow

class GetWeatherUseCase (private val repository: WeatherRepository) {

    operator fun invoke(dt:Int): Flow<Weather> = repository.getWeather(dt)
}
