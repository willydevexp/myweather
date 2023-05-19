package com.example.myweather.ui.weather

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweather.domain.Error
import com.example.myweather.framework.toError
import com.example.myweather.usecases.location.GetLocationNameUseCase
import com.example.myweather.usecases.weather.GetWeatherOfLocationUseCase
import com.example.myweather.usecases.weather.RequestWeatherOfLocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(safeStateHandle: SavedStateHandle,
                                           private val getLocationNameUseCase: GetLocationNameUseCase,
                                           private val requestWeatherOfLocationUseCase: RequestWeatherOfLocationUseCase,
                                           private val getWeatherOfLocationUseCase: GetWeatherOfLocationUseCase
) : ViewModel() {

    private val locationId = WeatherFragmentArgs.fromSavedStateHandle(safeStateHandle).locationId

    data class UiState(
        val isRefreshing: Boolean = false,
        val locationName: String = "",
        val weatherList: List<com.example.myweather.domain.Weather>? = null,
        val error: Error? = null
    )

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        if (_state.value.weatherList==null) {
            getLocationName()
            refresh()
        } else
            getWeather()
    }

    fun getWeather() {
        viewModelScope.launch {
            getWeatherOfLocationUseCase(locationId)
                .catch { cause -> _state.update { it.copy(error = cause.toError()) } }
                .collect { weatherList -> _state.update { _state.value.copy(weatherList = weatherList) } }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _state.update { _state.value.copy (isRefreshing = true) }
            val error = requestWeatherOfLocationUseCase (locationId)
            getWeather()
            _state.update { _state.value.copy(isRefreshing = false, error = error) }
        }
    }

    fun getLocationName () {
        viewModelScope.launch {
            val locationName = getLocationNameUseCase(locationId)
            _state.update { UiState(locationName = locationName) }
        }
    }

}


