package com.example.myweather.usecases.location

import com.example.myweather.data.AppRepository
import javax.inject.Inject

class AddLocationUseCase @Inject constructor (private val repository: AppRepository) {

    suspend operator fun invoke(locationName: String) {
        repository.addLocation(locationName)
    }

}