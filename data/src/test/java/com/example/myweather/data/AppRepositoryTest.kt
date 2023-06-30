package com.example.myweather.data

import arrow.core.Either
import arrow.core.right
import com.example.myweather.data.datasource.LocalDataSource
import com.example.myweather.data.datasource.WeatherRemoteDataSource
import com.example.myweather.domain.DomainLocation
import com.example.myweather.domain.Weather
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever


@RunWith(MockitoJUnitRunner::class)
class AppRepositoryTest {
    @Mock
    lateinit var locationServiceRepository: LocationServiceRepository

    @Mock
    lateinit var localDataSource: LocalDataSource

    @Mock
    lateinit var remoteDataSource: WeatherRemoteDataSource

    private lateinit var appRepository: AppRepository

    private val localLocations = flowOf(listOf(
        LocationServiceRepository.DEFAULT_LOCATION,
        LocationServiceRepository.MADRID_LOCATION,
        LocationServiceRepository.BARCELONA_LOCATION)
    )

    private val remoteLocations = flowOf(listOf(
        LocationServiceRepository.MADRID_LOCATION,
        LocationServiceRepository.BARCELONA_LOCATION)
    )

    private val localWeatherList = flowOf(listOf(sampleWeather.copy(1)))

    @Before
    fun setUp() {
        whenever(localDataSource.locationList).thenReturn(localLocations)
        appRepository = AppRepository(locationServiceRepository, localDataSource, remoteDataSource)
    }

    @Test
    fun `Locations are taken from local data source if available`(): Unit = runBlocking {
        val locationList = appRepository.locationList
        assertEquals(localLocations, locationList)
    }

    @Test
    fun `Last known location is saved to local data source`(): Unit = runBlocking {
        whenever(locationServiceRepository.findLastLocation()).thenReturn(LocationServiceRepository.DEFAULT_LOCATION)
        val location = appRepository.getLastLocation()
        verify(localDataSource).addLocation(location)
    }


    @Test
    fun `Given a valid location name, the location is added to the repository`() = runBlocking {
        val locationName = "Madrid"
        val location = LocationServiceRepository.MADRID_LOCATION
        whenever(remoteDataSource.findLocation(locationName)).thenReturn(location.right())

       appRepository.addLocation(locationName)

        verify(localDataSource).addLocation(location)
    }

    @Test
    fun `Deleted location is deleted from local data source`(): Unit = runBlocking {
        appRepository.delLocation(1)
        verify(localDataSource).delLocation(1)
    }


    @Test
    fun `Getting weather of location given the id from local data source` (): Unit = runBlocking {
        val weather = flowOf(listOf(sampleWeather.copy(locationId = 1)))
        whenever(localDataSource.getWeatherOfLocation(1)).thenReturn(weather)

        val result = appRepository.getWeatherOfLocation(1)

        assertEquals(weather, result)
    }

}

private val sampleWeather = Weather (
    0,
    20.0,
    19.0,
70,
    1024,
    100.0,
    "Sunny",
    "icon.png",
    1
)