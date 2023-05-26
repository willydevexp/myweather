package com.example.myweather.ui.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweather.domain.DomainLocation
import com.example.myweather.domain.Error
import com.example.myweather.usecases.location.AddLocationUseCase
import com.example.myweather.usecases.location.DelLocationUseCase
import com.example.myweather.usecases.location.GetLastLocationUseCase
import com.example.myweather.usecases.location.GetLocationListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val getlastLocationUseCase: GetLastLocationUseCase,
    private val getLocationListUseCase: GetLocationListUseCase,
    private val addLocationUseCase: AddLocationUseCase,
    private val delLocationUseCase: DelLocationUseCase
) : ViewModel() {

    data class UiState(
        val lastLocation : DomainLocation? = null,
        val locationList : List<DomainLocation>? = null,
        val error: Error? = null
    )

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getLocationListUseCase().collect() { locationList -> _state.update { UiState(locationList = locationList) }}
        }
    }

    fun getLastLocation () {
        viewModelScope.launch {
            val location = getlastLocationUseCase()
            _state.update { state.value.copy(lastLocation = location) }
        }
    }

    fun addLocation (locationName: String)  {
        viewModelScope.launch {
            val error = addLocationUseCase(locationName)
            _state.update { _state.value.copy(error = error) }
        }
    }

    fun delLocation (locationId: Int)  {
        viewModelScope.launch {
            delLocationUseCase(locationId)
        }
    }


}

