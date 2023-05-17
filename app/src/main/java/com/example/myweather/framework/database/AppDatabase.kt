package com.example.myweather.framework.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [EntityWeather::class, EntityLocation::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appDao(): AppDao
}
