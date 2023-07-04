package com.example.myweather.ui.weather

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.example.myweather.apptestshared.buildEntityLocationList
import com.example.myweather.apptestshared.buildEntityWeatherList
import com.example.myweather.apptestshared.buildRepositoryWith
import com.example.myweather.framework.database.EntityLocation
import com.example.myweather.framework.database.EntityWeather
import com.example.myweather.testRules.CoroutinesTestRule
import com.example.myweather.usecases.location.GetLocationNameUseCase
import com.example.myweather.usecases.weather.GetWeatherOfLocationUseCase
import com.example.myweather.usecases.weather.RequestWeatherOfLocationUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class WeatherIntegrationTests {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Test
    fun `weather of location is loaded from server when refresh`() = runTest {
        val locationList = buildEntityLocationList(1)
        val weatherList = buildEntityWeatherList(1)
        val vm = buildWeatherViewModelWith(1, locationList, weatherList)

        vm.refresh()

        vm.state.test {
            assertEquals(WeatherViewModel.UiState(), awaitItem())
            assertEquals(WeatherViewModel.UiState(locationName = "London - GB"), awaitItem())
            assertEquals(WeatherViewModel.UiState(isRefreshing = true, locationName = "London - GB"), awaitItem())
            assertEquals(WeatherViewModel.UiState(isRefreshing = false, locationName = "London - GB"), awaitItem())

            val remoteWeatherList = awaitItem().weatherList!!
            assertEquals(1, remoteWeatherList[0].locationId)

            cancel()
        }
    }


    private fun buildWeatherViewModelWith(
        locationId: Int,
        locationList: List<EntityLocation> = emptyList(),
        weatherList: List<EntityWeather> = emptyList()
    ): WeatherViewModel {
        val appRepository = buildRepositoryWith(locationList, weatherList)
        val savedStateHandle = SavedStateHandle().apply {set("locationId", locationId)}
        val getLocationNameUseCase = GetLocationNameUseCase (appRepository)
        val requestWeatherOfLocationUseCase = RequestWeatherOfLocationUseCase (appRepository)
        val getWeatherOfLocationUseCase = GetWeatherOfLocationUseCase (appRepository)
        return WeatherViewModel(savedStateHandle, getLocationNameUseCase, requestWeatherOfLocationUseCase, getWeatherOfLocationUseCase)
    }

}