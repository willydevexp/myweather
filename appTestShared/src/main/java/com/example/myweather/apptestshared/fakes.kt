package com.example.myweather.apptestshared

import com.example.myweather.data.LocationServiceRepository
import com.example.myweather.data.PermissionChecker
import com.example.myweather.data.datasource.LocationServiceDataSource
import com.example.myweather.domain.DomainLocation
import com.example.myweather.framework.database.AppDao
import com.example.myweather.framework.database.EntityLocation
import com.example.myweather.framework.database.EntityWeather
import com.example.myweather.framework.server.DailyWeatherResponse
import com.example.myweather.framework.server.DayWeather
import com.example.myweather.framework.server.FindLocationResponse
import com.example.myweather.framework.server.RemoteService
import com.example.myweather.framework.server.Temp
import com.example.myweather.framework.server.WeatherType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow


class FakeAppDao(locationList: List<EntityLocation>, weatherList: List<EntityWeather>) : AppDao {

    private val inMemoryLocationList = MutableStateFlow(locationList)
    private val inMemoryWeatherList = MutableStateFlow(weatherList)

    private lateinit var findWeatherListFlow: MutableStateFlow<List<EntityWeather>>
    private lateinit var findWeatherFlow: MutableStateFlow<EntityWeather>


    override fun getAllWeather() = inMemoryWeatherList

    override fun getWeatherOfLocation(locationId: Int): Flow<List<EntityWeather>> {
        findWeatherListFlow = MutableStateFlow(inMemoryWeatherList.value.filter { it.locationId == locationId })
        return findWeatherListFlow
    }

    override fun getWeather(dt: Int): Flow<EntityWeather> {
        findWeatherFlow = MutableStateFlow(inMemoryWeatherList.value.first { it.dt == dt })
        return  findWeatherFlow
    }


    override suspend fun weatherCount() = inMemoryWeatherList.value.count()

    override suspend fun insertWeatherList(weatherList: List<EntityWeather>) {
        inMemoryWeatherList.value = weatherList
    }

    override suspend fun deleteWeatherOfLocation(locationId: Int) {
        findWeatherListFlow = MutableStateFlow(inMemoryWeatherList.value.filter { it.locationId == locationId })
        inMemoryWeatherList.value.minus(findWeatherListFlow.value.first { it.locationId == locationId })
    }

    override suspend fun locationCount() = inMemoryLocationList.value.count()

    override fun getLocationList() = inMemoryLocationList

    override suspend fun getLocation(id: Int) = inMemoryLocationList.value.first { it.id == id }

    override suspend fun insertLocation(location: EntityLocation) {
        inMemoryWeatherList.value.plus(location)
    }

    override suspend fun deleteLocation(id: Int) {
        inMemoryWeatherList.value.minus(findWeatherListFlow.value.first { it.locationId == id })
    }


}

class FakeRemoteService() : RemoteService {
    override suspend fun getDailyWeather(
        lat: Double,
        lon: Double,
        appid: String,
        cnt: Int,
        units: String
    ) = DailyWeatherResponse (listOf(DayWeather(
        1688385600,
         54,
         1010,
        5.0,
        Temp(max=20.43, min=14.76),
        listOf(WeatherType("clear sky", "01d"))
    )))

    override suspend fun findLocation(
        cityName: String,
        appìd: String,
        limit: Int
    ): FindLocationResponse = FindLocationResponse()

}

class FakeLocationServiceDataSource : LocationServiceDataSource {

    override suspend fun findLastLocation(): DomainLocation? {
        return LocationServiceRepository.DEFAULT_LOCATION
    }

    override suspend fun findLocation(locationName: String):DomainLocation? {
        when (locationName) {
            LocationServiceRepository.MADRID_LOCATION.name -> return LocationServiceRepository.MADRID_LOCATION
            LocationServiceRepository.BARCELONA_LOCATION.name -> return LocationServiceRepository.BARCELONA_LOCATION
            LocationServiceRepository.DEFAULT_LOCATION.name -> return LocationServiceRepository.DEFAULT_LOCATION
            else -> return null
        }
    }
}

class FakePermissionChecker : PermissionChecker {
    var permissionGranted = true

    override fun check(permission: PermissionChecker.Permission) = permissionGranted
}