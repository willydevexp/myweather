package com.example.myweather.usecases.location

import com.example.myweather.data.AppRepository
import com.example.myweather.domain.DomainLocation
import kotlinx.coroutines.flow.Flow

class GetLocationNameUseCase (private val repository: AppRepository) {

    suspend operator fun invoke(locationId : Int): String = repository.getLocationName(locationId)
}