package com.example.myweather.usecases.weather

import com.example.myweather.data.AppRepository
import com.example.myweather.domain.Weather
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWeatherOfLocationUseCase @Inject constructor(private val repository: AppRepository) {

    operator fun invoke(locationId: Int): Flow<List<Weather>> {
        return repository.getWeatherOfLocation(locationId)
    }
}