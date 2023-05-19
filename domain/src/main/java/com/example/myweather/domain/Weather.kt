package com.example.myweather.domain

data class Weather (
    val dt: Int,
    val tempMax: Double,
    val tempMin: Double,
    val humidity: Int,
    val pressure: Int,
    val speed: Double,
    val description: String,
    val icon: String,
    val locationId: Int
)
