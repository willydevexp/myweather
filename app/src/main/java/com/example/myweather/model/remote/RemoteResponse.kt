package com.example.myweather.model.remote

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


data class DailyWeather(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<DayWeather>,
    val message: Double
)

data class City(
    val coord: Coord,
    val country: String,
    val id: Int,
    val name: String,
    val population: Int,
    val timezone: Int
)

data class Coord(
    val lat: Double,
    val lon: Double
)


@Parcelize
data class DayWeather (
    val clouds: Int,
    val deg: Int,
    val dt: Int,
    val feels_like: FeelsLike,
    val gust: Double,
    val humidity: Int,
    val pop: Double,
    val pressure: Int,
    val rain: Double,
    val speed: Double,
    val sunrise: Int,
    val sunset: Int,
    val temp: Temp,
    val weather: List<WeatherType>
) : Parcelable


@Parcelize
data class WeatherType(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
) : Parcelable

@Parcelize
data class Temp(
    val day: Double,
    val eve: Double,
    val max: Double,
    val min: Double,
    val morn: Double,
    val night: Double
) : Parcelable

@Parcelize
data class FeelsLike(
    val day: Double,
    val eve: Double,
    val morn: Double,
    val night: Double
) : Parcelable

