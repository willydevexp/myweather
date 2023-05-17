package com.example.myweather.framework

import android.annotation.SuppressLint
import android.app.Application
import android.location.Address
import android.location.Geocoder
import android.location.Location
import com.example.myweather.data.datasource.LocationServiceDataSource
import com.example.myweather.domain.DomainLocation
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class PlayServicesLocationServiceDataSource(application: Application) : LocationServiceDataSource {
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

    override suspend fun findLocation(locationName: String): DomainLocation? {
        val addresses = this.let {
            geocoder.getFromLocationName(locationName,1)
        }
        return addresses?.firstOrNull()?.toDomainLocation()
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

    // Location: Ultima localización por GPS (findLastLocation)
    // El id será siempre -1 para evitar añadirlo a la lista de localizaciones
    private fun Location?.toDomainLocation(): DomainLocation? {
        val domainlocation = this?.let {
            DomainLocation(-1 ,latitude, longitude, toCountryCode(), toLocationName())
        }
        return domainlocation
    }

    // Address: Dirección obtenida a partir del nombre de una localizacion (findLocation)
    // El id será 0 para que se autoincremente al añadir la localizaicón a la BD
    private fun Address.toDomainLocation(): DomainLocation {
        return DomainLocation (
            0,
            latitude,
            longitude,
            countryCode,
            locality
        )
    }
}


