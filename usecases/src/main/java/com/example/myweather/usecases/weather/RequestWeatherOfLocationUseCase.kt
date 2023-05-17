package com.example.myweather.usecases.weather

import com.example.myweather.data.AppRepository
import com.example.myweather.domain.Error


class RequestWeatherOfLocationUseCase (private val repository: AppRepository) {

    suspend operator fun invoke(locationId: Int): Error? {
        return repository.requestWeatherOfLocation(locationId)
    }
}