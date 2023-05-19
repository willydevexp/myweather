package com.example.myweather.usecases.weather

import com.example.myweather.data.AppRepository
import com.example.myweather.domain.Error
import javax.inject.Inject


class RequestWeatherOfLocationUseCase @Inject constructor(private val repository: AppRepository) {

    suspend operator fun invoke(locationId: Int): Error? {
        return repository.requestWeatherOfLocation(locationId)
    }
}