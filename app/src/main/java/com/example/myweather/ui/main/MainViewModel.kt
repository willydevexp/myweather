package com.example.myweather.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myweather.common.Error
import com.example.myweather.common.toError
import com.example.myweather.model.WeatherRepository
import com.example.myweather.model.database.Weather
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(private val weatherRepository: WeatherRepository) : ViewModel() {

    data class UiState(
        val isRefreshing: Boolean = false,
        val cityName: String = "",
        val weatherList: List<Weather>? = null,
        val navigateTo: Weather? = null,
        val error: Error? = null
    )

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val cityName = weatherRepository.getCityName()
            // Actualizamos la ciudad y el tiempo cuando se actualice el repositorio
            weatherRepository.weatherList
                .catch { cause -> _state.update { it.copy(error = cause.toError()) } }
                .collect() { weatherList ->  _state.update {UiState(cityName = cityName, weatherList = weatherList) }
            }
        }
        // Forzamos una actualización para tener los datos actualizados
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _state.update { _state.value.copy (isRefreshing = true) }
            val error = weatherRepository.requestWeatherList(true)
            _state.update { _state.value.copy(isRefreshing = false, error = error) }
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