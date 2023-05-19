package com.example.myweather.usecases.location

import com.example.myweather.data.AppRepository
import com.example.myweather.domain.DomainLocation
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLocationListUseCase @Inject constructor(private val repository: AppRepository) {

    operator fun invoke(): Flow<List<DomainLocation>> = repository.locationList
}