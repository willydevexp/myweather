package com.example.myweather.usecases.weather

import com.example.myweather.data.AppRepository
import com.example.myweather.domain.Weather
import kotlinx.coroutines.flow.Flow

class GetWeatherUseCase (private val repository: AppRepository) {

    operator fun invoke(dt:Int): Flow<Weather> = repository.getWeather(dt)
}
