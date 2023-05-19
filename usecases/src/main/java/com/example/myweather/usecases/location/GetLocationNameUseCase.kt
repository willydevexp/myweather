package com.example.myweather.usecases.location

import com.example.myweather.data.AppRepository
import com.example.myweather.domain.DomainLocation
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLocationNameUseCase @Inject constructor(private val repository: AppRepository) {

    suspend operator fun invoke(locationId : Int): String = repository.getLocationName(locationId)
}