package com.example.myweather.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myweather.domain.Weather
import com.example.myweather.usecases.GetWeatherUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(cityName: String, weatherDT: Int, getWeatherUseCase: GetWeatherUseCase) : ViewModel() {

    class UiState(
        val cityName: String = "",
        val weather: Weather? = null)

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getWeatherUseCase(weatherDT).collect {
                _state.value = UiState(cityName,it)
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
class DetailViewModelFactory(
    private val cityName: String,
    private val weatherDT: Int,
    private val getWeatherUseCase: GetWeatherUseCase) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailViewModel(cityName, weatherDT, getWeatherUseCase) as T
    }
}
