package com.example.myweather.data

import arrow.core.Either
import com.example.myweather.data.datasource.LocalDataSource
import com.example.myweather.data.datasource.WeatherRemoteDataSource
import com.example.myweather.domain.DomainLocation
import com.example.myweather.domain.Weather
import com.example.myweather.domain.Error
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class AppRepository @Inject constructor (
    private val locationServiceRepository: LocationServiceRepository,
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: WeatherRemoteDataSource) {

    val locationList = localDataSource.locationList

    suspend fun getLastLocation () : DomainLocation {
        val location = locationServiceRepository.findLastLocation()
        localDataSource.addLocation(location)
        return location
    }

    suspend fun findLocation (locationName: String) : Either<Error, DomainLocation> {
        return remoteDataSource.findLocation(locationName)
    }

    suspend fun addLocation(locationName: String) : Error? {
        val location = findLocation(locationName)
        location.fold(ifLeft = { return it }) {
            localDataSource.addLocation(it)
            return null
        }
    }

    suspend fun delLocation(locationId: Int) {
        localDataSource.delLocation(locationId)
    }

    suspend fun getLocationName (locationId: Int) : String{
        val location = localDataSource.getLocation(locationId)
        return "${location.name} - ${location.countryCode}"
    }

    suspend fun requestWeatherOfLocation (locationId: Int) : Error ? {
        // Obtenemos la localizacion
        val location = localDataSource.getLocation(locationId)
        // Obtenemos el tiempo de la localización
        val dailyWeather = remoteDataSource.getDailyWeather(location)
        // Guardamos el tiempo de la localización o devolvemos error
        dailyWeather.fold(ifLeft = { return it }) {
            localDataSource.insertWeatherList(locationId, it)
            return null
        }
    }

    suspend fun getWeatherOfLocation(locationId: Int): Flow<List<Weather>> =
        localDataSource.getWeatherOfLocation(locationId)


    fun getWeather(dt: Int): Flow<Weather> = localDataSource.getWeather(dt)


}




