package com.example.myweather.data.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Weather(
    @PrimaryKey() val dt: Int,
    val tempMax: Double,
    val tempMin: Double,
    val humidity: Int,
    val pressure: Int,
    val speed: Double,
    val description: String,
    val icon: String
) : Parcelable
