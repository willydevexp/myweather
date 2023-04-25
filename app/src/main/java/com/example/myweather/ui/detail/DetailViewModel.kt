package com.example.myweather.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myweather.data.WeatherRepository
import com.example.myweather.data.database.Weather
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(cityName: String, weatherDT: Int, private val repository: WeatherRepository) : ViewModel() {

    class UiState(
        val cityName: String = "",
        val weather: Weather? = null)

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getWeather(weatherDT).collect {
                _state.value = UiState(cityName,it)
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
class DetailViewModelFactory(private val cityName: String, private val weatherDT: Int, private val repository: WeatherRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailViewModel(cityName, weatherDT, repository) as T
    }
}