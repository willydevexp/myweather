package com.example.myweather.model

import android.Manifest
import android.annotation.SuppressLint
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class LocationRepository(activity: AppCompatActivity) {

    companion object {
        private const val DEFAULT_REGION = "US"
        private const val DEFAULT_CITY = "Unknown"
    }

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
    private val coarsePermissionChecker = PermissionChecker(
        activity,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    private val geocoder = Geocoder(activity)

    suspend fun getLastLocation(): Location? = findLastLocation();
    suspend fun findLastRegion(): String = findLastLocation().toRegion()
    suspend fun findLastCity(): String = findLastLocation().toCity()

    private suspend fun findLastLocation(): Location? {
        val success = coarsePermissionChecker.request()
        return if (success) lastLocationSuspended() else null
    }

    @SuppressLint("MissingPermission")
    private suspend fun lastLocationSuspended(): Location? =
        suspendCancellableCoroutine { continuation ->
            fusedLocationClient.lastLocation
                .addOnCompleteListener {
                    continuation.resume(it.result)
                }
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