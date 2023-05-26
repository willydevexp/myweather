package com.example.myweather.framework.server

class FindLocationResponse : ArrayList<FindLocationResponseItem>()

data class FindLocationResponseItem(
    val country: String,
    val lat: Double,
    val lon: Double,
    val name: String,
    val state: String
)

