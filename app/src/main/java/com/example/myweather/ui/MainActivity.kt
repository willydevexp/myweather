package com.example.myweather.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myweather.R
import com.example.myweather.databinding.ActivityMainBinding
import com.example.myweather.model.RemoteConnection
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class MainActivity : AppCompatActivity() {

    private val requestPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            lifecycleScope.launch {
                val location = getLocation(isGranted)
                val remoteResponse = location?.let {
                    RemoteConnection.service.getWeather(
                        it.latitude,
                        it.longitude,
                        getString(R.string.api_key)
                    )
                }
                if (remoteResponse != null) {
                    adapter.weatherList = remoteResponse.list
                } else {
                    Log.e ("MainActivity", "ERROR: Error getting weather.")
                }
            }
        }

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val adapter = WeatherListAdapter {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.WEATHER, it)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvWeather.adapter = adapter

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    private suspend fun getLocation(granted: Boolean): Location? {
        return if (granted) findLastLocation() else null
    }

    @SuppressLint("MissingPermission")
    private suspend fun findLastLocation(): Location? =
        suspendCancellableCoroutine { continuation ->
            fusedLocationClient.lastLocation
                .addOnCompleteListener {
                    continuation.resume(it.result)
                }
        }

    private fun getRegionFromLocation(location: Location?): String {
        val geocoder = Geocoder(this@MainActivity)
        val fromLocation = location?.let {
            geocoder.getFromLocation(
                location.latitude,
                location.longitude,
                1
            )
        }
        return fromLocation?.firstOrNull()?.countryCode ?: "US"
    }

    private fun getCityNameFromLocation(location: Location?): String {
        val geocoder = Geocoder(this@MainActivity)
        val fromLocation = location?.let {
            geocoder.getFromLocation(
                location.latitude,
                location.longitude,
                1
            )
        }
        return fromLocation?.firstOrNull()?.countryName ?: "Unknouwn"
    }
}