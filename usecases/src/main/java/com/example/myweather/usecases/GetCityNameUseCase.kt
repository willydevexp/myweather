package com.example.myweather.usecases

import com.example.myweather.data.WeatherRepository

class GetCityNameUseCase(private val repository: WeatherRepository) {

    suspend operator fun invoke(): String {
        return repository.getCityName()
    }
}
