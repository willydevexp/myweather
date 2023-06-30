package com.example.myweather.apptestshared

import com.example.myweather.data.AppRepository
import com.example.myweather.data.LocationServiceRepository
import com.example.myweather.framework.database.EntityLocation
import com.example.myweather.framework.database.EntityWeather
import com.example.myweather.framework.database.RoomDataSource
import com.example.myweather.framework.server.DayWeather
import com.example.myweather.framework.server.WeatherServerDataSource


fun buildRepositoryWith(
    locationList: List<EntityLocation>,
    weatherList: List<EntityWeather>,
    remoteWeatherData: List<DayWeather>
): AppRepository {
    val locationDataSource = FakeLocationServiceDataSource()
    val permissionChecker = FakePermissionChecker()
    val locationServiceRepository = LocationServiceRepository(locationDataSource, permissionChecker)
    val localDataSource = RoomDataSource(FakeAppDao(locationList, weatherList))
    val remoteDataSource = WeatherServerDataSource("1234", FakeRemoteService())
    return AppRepository(locationServiceRepository, localDataSource, remoteDataSource)
}

fun buildLocationList (vararg id: Int) = id.map {
    EntityLocation(
        it,
        51.5073219,
        -0.127647,
        "GB",
        "London"
    )
}


fun buildWeatherList(vararg locationId: Int) = locationId.map {
    EntityWeather(
        1,
        25.0,
        20.0,
        90,
        1024,
        80.0,
        "sunny",
        "sunny",
        it
    )
}

