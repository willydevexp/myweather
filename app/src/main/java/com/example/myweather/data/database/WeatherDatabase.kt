package com.example.myweather.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Weather::class], version = 1, exportSchema = false)
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun weatherDao(): WeatherDao
}