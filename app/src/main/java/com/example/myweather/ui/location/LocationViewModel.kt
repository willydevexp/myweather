package com.example.myweather.ui.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myweather.domain.DomainLocation
import com.example.myweather.usecases.location.AddLocationUseCase
import com.example.myweather.usecases.location.DelLocationUseCase
import com.example.myweather.usecases.location.GetLastLocationUseCase
import com.example.myweather.usecases.location.GetLocationListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LocationViewModel(
    private val getlastLocationUseCase: GetLastLocationUseCase,
    private val getLocationListUseCase: GetLocationListUseCase,
    private val addLocationUseCase: AddLocationUseCase,
    private val delLocationUseCase: DelLocationUseCase
) : ViewModel() {

    data class UiState(
        val lastLocation : DomainLocation? = null,
        val locationList : List<DomainLocation>? = null
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
            addLocationUseCase(locationName)
        }
    }

    fun delLocation (idLocation: Int)  {
        viewModelScope.launch {
            delLocationUseCase(idLocation)
        }
    }


}

@Suppress("UNCHECKED_CAST")
class LocationViewModelFactory(private val getlastLocationUseCase: GetLastLocationUseCase,
                               private val getLocationListUseCase: GetLocationListUseCase,
                               private val addLocationUseCase: AddLocationUseCase,
                               private val delLocationUseCase: DelLocationUseCase
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LocationViewModel(getlastLocationUseCase, getLocationListUseCase, addLocationUseCase, delLocationUseCase) as T
    }
}