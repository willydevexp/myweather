package com.example.myweather.usecases.location

import com.example.myweather.data.AppRepository
import com.example.myweather.domain.Error
import javax.inject.Inject

class AddLocationUseCase @Inject constructor (private val repository: AppRepository) {

    suspend operator fun invoke(locationName: String): Error? {
        return repository.addLocation(locationName)
    }

}