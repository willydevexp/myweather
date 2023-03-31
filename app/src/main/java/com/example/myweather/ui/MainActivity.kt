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
import com.example.myweather.model.LocationRepository
import com.example.myweather.model.RemoteConnection
import com.example.myweather.model.WeatherRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class MainActivity : AppCompatActivity() {

    private val weatherRepository by lazy { WeatherRepository(this) }

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

        lifecycleScope.launch {
            // Cargamos el adapter con la lista del tiempo diario para la ciudad geolocalizada
            adapter.weatherList = weatherRepository.getDailyWeather()!!.list

            // Ponemos el nombre de la ciudad en la barra de título
            val title = supportActionBar?.title
            val cityName = weatherRepository.getDailyWeather()!!.city.name
            supportActionBar?.title = "$title - $cityName"

        }

    }


}