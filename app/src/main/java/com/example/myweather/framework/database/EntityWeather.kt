package com.example.myweather.framework.database

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(primaryKeys = ["dt", "locationId"])
data class EntityWeather(
    val dt: Int,
    val tempMax: Double,
    val tempMin: Double,
    val humidity: Int,
    val pressure: Int,
    val speed: Double,
    val description: String,
    val icon: String,
    val locationId: Int
) : Parcelable
