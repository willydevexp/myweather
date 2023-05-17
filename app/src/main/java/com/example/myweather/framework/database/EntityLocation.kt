package com.example.myweather.framework.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class EntityLocation(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val lat: Double,
    val lon: Double,
    val countryCode: String,
    val name: String,
): Parcelable
