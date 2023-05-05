package com.example.myweather.framework.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WeatherTable::class], version = 1, exportSchema = false)
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun weatherDao(): WeatherDao
}
