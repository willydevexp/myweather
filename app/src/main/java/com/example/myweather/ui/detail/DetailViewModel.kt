package com.example.myweather.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweather.domain.Weather
import com.example.myweather.usecases.location.GetLocationNameUseCase
import com.example.myweather.usecases.weather.GetWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(safeStateHandle: SavedStateHandle,
                                          private val getLocationNameUseCase: GetLocationNameUseCase,
                                          getWeatherUseCase: GetWeatherUseCase) : ViewModel() {

    private val weatherDT = DetailFragmentArgs.fromSavedStateHandle(safeStateHandle).weatherDT

    data class UiState(
        val locationName: String = "",
        val weather: Weather? = null
    )

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getWeatherUseCase(weatherDT).collect {
                _state.value = UiState(weather = it)
                getLocationName()
            }
        }
    }

    fun getLocationName() {
        viewModelScope.launch {
            val locationName = state.value.weather?.locationId?.let { getLocationNameUseCase(it) }
            if (locationName!=null) {
                _state.update { _state.value.copy(locationName = locationName) }
            }
        }
    }


}

