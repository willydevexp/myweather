package com.example.myweather.data


import com.example.myweather.data.PermissionChecker.Permission.COARSE_LOCATION
import com.example.myweather.data.datasource.LocalDataSource
import com.example.myweather.data.datasource.LocationServiceDataSource
import com.example.myweather.domain.DomainLocation
import javax.inject.Inject
import javax.xml.stream.Location

class LocationServiceRepository @Inject constructor (
    private val locationServiceDataSource: LocationServiceDataSource,
    private val permissionChecker: PermissionChecker
) {

    companion object {
        val DEFAULT_LOCATION = DomainLocation(-1,37.422131, -122.084801, "US", "Mountain View")
        val MADRID_LOCATION = DomainLocation(1, 40.416775, -3.703790, "ES", "Madrid")
        val BARCELONA_LOCATION = DomainLocation(2, 41.390205, 2.154007, "ES", "Barcelona")
    }

    suspend fun findLastLocation(): DomainLocation {
        return if (permissionChecker.check(COARSE_LOCATION)) {
            locationServiceDataSource.findLastLocation() ?: DEFAULT_LOCATION
        } else {
            DEFAULT_LOCATION
        }
    }

}
