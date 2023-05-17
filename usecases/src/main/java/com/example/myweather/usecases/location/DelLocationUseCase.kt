package com.example.myweather.usecases.location

import com.example.myweather.data.AppRepository
import com.example.myweather.domain.DomainLocation

class DelLocationUseCase (private val repository: AppRepository) {

    suspend operator fun invoke(idLocation: Int) {
        repository.delLocation(idLocation)
    }

}