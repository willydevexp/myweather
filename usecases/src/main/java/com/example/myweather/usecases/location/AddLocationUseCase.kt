package com.example.myweather.usecases.location

import com.example.myweather.data.AppRepository

class AddLocationUseCase (private val repository: AppRepository) {

    suspend operator fun invoke(locationName: String) {
        repository.addLocation(locationName)
    }

}