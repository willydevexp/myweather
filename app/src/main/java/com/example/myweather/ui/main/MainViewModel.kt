package com.example.myweather.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myweather.model.WeatherRepository
import com.example.myweather.model.database.Weather
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val weatherRepository: WeatherRepository) : ViewModel() {

    data class UiState(
        val loading: Boolean = false,
        val cityName: String = "",
        val weatherList: List<Weather>? = null,
        val navigateTo: Weather? = null
    )

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val cityName = weatherRepository.getCityName()
            weatherRepository.weatherList.collect() { weatherList ->
                _state.value = UiState(cityName = cityName, weatherList = weatherList)
            }

        }

    }

    private fun refresh() {
        viewModelScope.launch {
            _state.value = UiState(loading = true)
            weatherRepository.requestWeatherList()
        }
    }


    fun onWeatherClicked(weather: Weather) {
        _state.value = _state.value.copy(navigateTo = weather)
    }

    fun onNavigateDone() {
        _state.value = _state.value.copy(navigateTo = null)
    }

}

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(private val weatherRepository: WeatherRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(weatherRepository) as T
    }
}