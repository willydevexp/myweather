package com.example.myweather.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myweather.domain.Weather
import com.example.myweather.usecases.weather.GetWeatherUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(weatherDT: Int, getWeatherUseCase: GetWeatherUseCase) : ViewModel() {

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

@Suppress("UNCHECKED_CAST")
class DetailViewModelFactory(
    private val weatherDT: Int,
    private val getWeatherUseCase: GetWeatherUseCase
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailViewModel(weatherDT, getWeatherUseCase) as T
    }
}
