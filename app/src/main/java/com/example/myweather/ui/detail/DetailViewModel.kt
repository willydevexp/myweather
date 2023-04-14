package com.example.myweather.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myweather.model.DayWeather
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DetailViewModel(cityName: String, dayWeather: DayWeather) : ViewModel() {

    class UiState(
        val cityName: String,
        val dayWeather: DayWeather?)

    private val _state = MutableStateFlow(UiState(cityName, dayWeather))
    val state: StateFlow<UiState> = _state.asStateFlow()
}

@Suppress("UNCHECKED_CAST")
class DetailViewModelFactory(private val cityName: String, private val dayWeather: DayWeather) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailViewModel(cityName, dayWeather) as T
    }
}