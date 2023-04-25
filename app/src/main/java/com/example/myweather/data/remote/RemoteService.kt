package com.example.myweather.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface RemoteService {

    @GET("data/2.5/forecast/daily")
    suspend fun getDailyWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appid: String,
        @Query("cnt") cnt: Int = 7,
        @Query("units") units: String = "metric"
    ): DailyWeather

}