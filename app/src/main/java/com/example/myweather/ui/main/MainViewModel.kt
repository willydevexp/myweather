package com.example.myweather.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myweather.framework.toError
import com.example.myweather.usecases.GetCityNameUseCase
import com.example.myweather.usecases.GetWeatherListUseCase
import com.example.myweather.usecases.RequestWeatherListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val getCityNameUseCase: GetCityNameUseCase,
    private val getWeatherListUseCase: GetWeatherListUseCase,
    private val requestWeatherListUseCase: RequestWeatherListUseCase
) : ViewModel() {

    data class UiState(
        val isRefreshing: Boolean = false,
        val cityName: String = "",
        val weatherList: List<com.example.myweather.domain.Weather>? = null,
        val error: com.example.myweather.domain.Error? = null
    )

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val cityName = getCityNameUseCase()
            getWeatherListUseCase()
                .catch { cause -> _state.update { it.copy(error = cause.toError()) } }
                .collect() { weatherList ->  _state.update {UiState(cityName = cityName, weatherList = weatherList) }
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _state.update { _state.value.copy (isRefreshing = true) }
            val error = requestWeatherListUseCase(true)
            _state.update { _state.value.copy(isRefreshing = false, error = error) }
        }
    }

}

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(private val getCityNameUseCase: GetCityNameUseCase,
                           private val getWeatherListUseCase: GetWeatherListUseCase,
                           private val requestWeatherListUseCase: RequestWeatherListUseCase
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(getCityNameUseCase, getWeatherListUseCase, requestWeatherListUseCase) as T
    }
}
