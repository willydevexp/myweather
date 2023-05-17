package com.example.myweather.framework.database

import com.example.myweather.data.datasource.LocalDataSource
import com.example.myweather.domain.DomainLocation
import com.example.myweather.domain.Weather
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomDataSource(private val appDao: AppDao) : LocalDataSource {

    override val weatherList = appDao.getAllWeather().map { it.toDomainWeather() }

    override fun getWeather (dt: Int) = appDao.getWeather(dt).map { it.toDomainWeather() }
    override fun getWeatherOfLocation(locationId: Int): Flow<List<Weather>> =
        appDao.getWeatherOfLocation(locationId).map { it.toDomainWeather() }

    override suspend fun isWeatherListEmpty(): Boolean = appDao.weatherCount() == 0

    override suspend fun insertWeatherList(locationId: Int, weatherList: List<Weather>) {
        appDao.deleteWeatherOfLocation(locationId)
        appDao.insertWeatherList(weatherList.fromDomainWeather())
    }

    override val locationList: Flow<List<DomainLocation>> = appDao.getLocationList().map { it.toDomainLocation() }

    override suspend fun getLocation(locationId: Int): DomainLocation =
        appDao.getLocation(locationId).toDomainLocation()

    override suspend fun isLocationListEmpty(): Boolean = appDao.locationCount() == 0
    override suspend fun addLocation(location: DomainLocation) {
        appDao.insertLocation(location.fromDomainLocation())
    }

    override suspend fun delLocation(locationId: Int) {
        appDao.deleteLocation(locationId)
        appDao.deleteWeatherOfLocation(locationId)
    }
}

private fun List<EntityWeather>.toDomainWeather(): List<Weather> = map { it.toDomainWeather() }

private fun EntityWeather.toDomainWeather(): Weather =
    Weather(
        dt,
        tempMax,
        tempMin,
        humidity,
        pressure,
        speed,
        description,
        icon,
        idLocation
    )

private fun List<Weather>.fromDomainWeather(): List<EntityWeather> =
    map { it.fromDomainWeather() }

private fun Weather.fromDomainWeather(): EntityWeather = EntityWeather(
    dt,
    tempMax,
    tempMin,
    humidity,
    pressure,
    speed,
    description,
    icon,
    idLocation
)
private fun List<EntityLocation>.toDomainLocation(): List<DomainLocation> = map { it.toDomainLocation() }

private fun EntityLocation.toDomainLocation(): DomainLocation =
    DomainLocation(
        id,
        lat,
        lon,
        countryCode,
        name
    )

private fun List<DomainLocation>.fromDomainLocationModel(): List<EntityLocation> =
    map { it.fromDomainLocation() }

private fun DomainLocation.fromDomainLocation(): EntityLocation = EntityLocation(
    id,
    lat,
    lon,
    countryCode,
    name
)