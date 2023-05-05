package com.example.myweather.usecases

import com.example.myweather.data.WeatherRepository
import com.example.myweather.domain.Error

class RequestWeatherListUseCase(private val repository: WeatherRepository) {

    suspend operator fun invoke(forceRefresh: Boolean): Error? {
        return repository.requestWeatherList(forceRefresh)
    }
}
