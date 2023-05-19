package com.example.myweather.data


import com.example.myweather.data.PermissionChecker.Permission.COARSE_LOCATION
import com.example.myweather.data.datasource.LocalDataSource
import com.example.myweather.data.datasource.LocationServiceDataSource
import com.example.myweather.domain.DomainLocation
import javax.inject.Inject

class LocationServiceRepository @Inject constructor (
    private val locationServiceDataSource: LocationServiceDataSource,
    private val permissionChecker: PermissionChecker
) {

    companion object {
        private val DEFAULT_LOCATION = DomainLocation(0,0.0, 0.0, "", "Unknown")
    }

    suspend fun findLastLocation(): DomainLocation {
        return if (permissionChecker.check(COARSE_LOCATION)) {
            locationServiceDataSource.findLastLocation() ?: DEFAULT_LOCATION
        } else {
            DEFAULT_LOCATION
        }
    }

    suspend fun findLocation(locationName: String) : DomainLocation {
        return locationServiceDataSource.findLocation(locationName) ?: DEFAULT_LOCATION
    }
}
