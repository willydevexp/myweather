package com.example.myweather.model

import retrofit2.http.GET
import retrofit2.http.Query

interface RemoteService {

    @GET("data/2.5/forecast/daily")
    suspend fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appid: String,
        @Query("cnt") cnt: Int = 7,
        @Query("units") units: String = "metric"
    ): RemoteResponse

}