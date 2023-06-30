package com.example.myweather.framework.server

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
    ): DailyWeatherResponse


    @GET("geo/1.0/direct")
    suspend fun findLocation(
        @Query("q") cityName: String,
        @Query("appid") appìd: String,
        @Query("limit") limit: Int = 1
    ): FindLocationResponse



}
