package com.example.myweather.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myweather.model.DayWeather
import com.example.myweather.model.WeatherRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MainViewModel(private val weatherRepository: WeatherRepository) : ViewModel() {

    data class UiState(
        val loading: Boolean = false,
        val cityName: String = "",
        val weatherList: List<DayWeather>? = null,
        val navigateTo: DayWeather? = null
    )

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        refresh()
    }

    private fun refresh() {
        viewModelScope.launch {
            weatherRepository.getDailyWeather()?.let {
                _state.value = UiState(loading = true)
                _state.value = UiState(cityName = it.city.name, weatherList = it.list)
            }
        }
    }


    fun onDayWeatherClicked(dayWeather: DayWeather) {
        _state.value = _state.value.copy(navigateTo = dayWeather)
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