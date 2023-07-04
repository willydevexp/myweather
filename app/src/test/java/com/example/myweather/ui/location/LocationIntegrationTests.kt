package com.example.myweather.ui.location

import app.cash.turbine.test
import com.example.myweather.apptestshared.buildEntityLocationList
import com.example.myweather.apptestshared.buildRepositoryWith
import com.example.myweather.framework.database.EntityLocation
import com.example.myweather.framework.database.EntityWeather
import com.example.myweather.testRules.CoroutinesTestRule
import com.example.myweather.usecases.location.AddLocationUseCase
import com.example.myweather.usecases.location.DelLocationUseCase
import com.example.myweather.usecases.location.GetLastLocationUseCase
import com.example.myweather.usecases.location.GetLocationListUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class LocationIntegrationTests {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Test
    fun `locations loaded from local source when available`() = runTest {
        val locallocationList = buildEntityLocationList(1, 2, 3)
        val vm = buildLocationViewModelWith(locallocationList, emptyList())

        vm.state.test {
            assertEquals(LocationViewModel.UiState(), awaitItem())

            val locationList = awaitItem().locationList!!
            assertEquals(1, locationList[0].id)
            assertEquals(2, locationList[1].id)
            assertEquals(3, locationList[2].id)

            cancel()
        }
    }

    private fun buildLocationViewModelWith(
        locationList: List<EntityLocation> = emptyList(),
        weatherList: List<EntityWeather> = emptyList()
    ): LocationViewModel {
        val appRepository = buildRepositoryWith(locationList, weatherList)
        val getlastLocationUseCase = GetLastLocationUseCase (appRepository)
        val getLocationListUseCase = GetLocationListUseCase (appRepository)
        val addLocationUseCase = AddLocationUseCase (appRepository)
        val delLocationUseCase = DelLocationUseCase (appRepository)
        return LocationViewModel(getlastLocationUseCase,getLocationListUseCase,addLocationUseCase,delLocationUseCase)
    }
}