package com.example.myweather.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweather.domain.Weather
import com.example.myweather.usecases.weather.GetWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(safeStateHandle: SavedStateHandle,
                                          getWeatherUseCase: GetWeatherUseCase) : ViewModel() {

    private val weatherDT = DetailFragmentArgs.fromSavedStateHandle(safeStateHandle).weatherDT

    class UiState(val weather: Weather? = null)

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getWeatherUseCase(weatherDT).collect {
                _state.value = UiState(it)
            }
        }
    }
}

