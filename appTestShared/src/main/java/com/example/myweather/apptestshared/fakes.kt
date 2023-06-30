package com.example.myweather.apptestshared

import android.content.res.Resources
import com.example.myweather.data.LocationServiceRepository
import com.example.myweather.data.PermissionChecker
import com.example.myweather.data.datasource.LocationServiceDataSource
import com.example.myweather.domain.DomainLocation
import com.example.myweather.framework.database.AppDao
import com.example.myweather.framework.database.EntityLocation
import com.example.myweather.framework.database.EntityWeather
import com.example.myweather.framework.server.DailyWeatherResponse
import com.example.myweather.framework.server.FindLocationResponse
import com.example.myweather.framework.server.RemoteService
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow



class FakeAppDao(locationList: List<EntityLocation>, weatherList: List<EntityWeather>) : AppDao {

    private val inMemoryLocationList = MutableStateFlow(locationList)
    private val inMemoryWeatherList = MutableStateFlow(weatherList)

    private lateinit var findWeatherListFlow: MutableStateFlow<List<EntityWeather>>
    private lateinit var findWeatherFlow: MutableStateFlow<EntityWeather>

    private lateinit var findLocationFlow: MutableStateFlow<EntityLocation>

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
    ): DailyWeatherResponse {
        val gson = Gson()
        val dailyWeather = Resources.getSystem().openRawResource(R.raw.daily_weather);
        return gson.fromJson(dailyWeather.toString(), DailyWeatherResponse::class.java)
    }

    override suspend fun findLocation(
        cityName: String,
        appìd: String,
        limit: Int
    ) : FindLocationResponse {
        val gson = Gson()
        val findLocation = Resources.getSystem().openRawResource(R.raw.find_location);
        return gson.fromJson(findLocation.toString(), FindLocationResponse::class.java)
    }


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