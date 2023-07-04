package com.example.myweather.framework.server

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


data class DailyWeatherResponse (
    val list: List<DayWeather>,
)


@Parcelize
data class DayWeather (
    val dt: Int,
    val humidity: Int,
    val pressure: Int,
    val speed: Double,
    val temp: Temp,
    val weather: List<WeatherType>
) : Parcelable

@Parcelize
data class WeatherType(
    val description: String,
    val icon: String,
) : Parcelable

@Parcelize
data class Temp(
    val max: Double,
    val min: Double,
) : Parcelable

