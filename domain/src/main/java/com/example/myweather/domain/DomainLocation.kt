package com.example.myweather.domain

data class DomainLocation (
    val id: Int,
    val lat: Double,
    val lon: Double,
    val countryCode: String,
    val name: String,
)
