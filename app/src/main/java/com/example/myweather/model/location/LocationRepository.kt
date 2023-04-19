package com.example.myweather.model.location

import android.Manifest
import android.app.Application
import android.location.Geocoder
import android.location.Location


class LocationRepository(application: Application) {

    companion object {
        private const val DEFAULT_REGION = "US"
        private const val DEFAULT_CITY = "Unknown"
    }

    private val locationDataSource: LocationDataSource = PlayServicesLocationDataSource(application)
    private val coarsePermissionChecker = PermissionChecker(
        application,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    private val geocoder = Geocoder(application)
    
    suspend fun getLastRegion(): String = findLastLocation().toRegion()
    suspend fun getLastCity(): String = findLastLocation().toCity()

    suspend fun findLastLocation(): Location? {
        val success = coarsePermissionChecker.check()
        return if (success) locationDataSource.findLastLocation() else null
    }

    private fun Location?.toRegion(): String {
        val addresses = this?.let {
            geocoder.getFromLocation(latitude, longitude, 1)
        }
        return addresses?.firstOrNull()?.countryCode ?: DEFAULT_REGION
    }

    private fun Location?.toCity(): String {
        val addresses = this?.let {
            geocoder.getFromLocation(latitude, longitude, 1)
        }
        return addresses?.firstOrNull()?.locality ?: DEFAULT_CITY
    }

}