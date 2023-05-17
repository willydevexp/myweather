package com.example.myweather.usecases.location

import com.example.myweather.data.AppRepository
import com.example.myweather.data.LocationServiceRepository
import com.example.myweather.domain.DomainLocation

class GetLastLocationUseCase (private val repository: AppRepository) {

    suspend operator fun invoke(): DomainLocation = repository.getLastLocation()
}