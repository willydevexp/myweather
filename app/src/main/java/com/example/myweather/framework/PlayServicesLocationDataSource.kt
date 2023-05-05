package com.example.myweather.framework

import android.annotation.SuppressLint
import android.app.Application
import android.location.Geocoder
import android.location.Location
import com.example.myweather.data.datasource.LocationDataSource
import com.example.myweather.domain.DomainLocation
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class PlayServicesLocationDataSource(application: Application) : LocationDataSource {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)
    private val geocoder = Geocoder(application)

    @SuppressLint("MissingPermission")
    override suspend fun findLastLocation(): DomainLocation? =
        suspendCancellableCoroutine { continuation ->
            fusedLocationClient.lastLocation
                .addOnCompleteListener {
                    continuation.resume(it.result.toDomainLocation())
                }
        }

    private fun Location?.toCountryCode(): String {
        val addresses = this?.let {
            geocoder.getFromLocation(latitude, longitude, 1)
        }
        return addresses?.firstOrNull()?.countryCode ?: ""
    }

    private fun Location?.toLocationName(): String {
        val addresses = this?.let {
            geocoder.getFromLocation(latitude, longitude, 1)
        }
        return addresses?.firstOrNull()?.locality ?: ""
    }

    private fun Location?.toDomainLocation(): DomainLocation? {
        val domainlocation = this?.let {
            DomainLocation(0, latitude, longitude, toCountryCode(), toLocationName())
        }
        return domainlocation
    }
}
