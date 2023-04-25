package com.example.myweather

import android.app.Application
import androidx.room.Room
import com.example.myweather.data.database.WeatherDatabase

class App : Application() {

    lateinit var db: WeatherDatabase
        private set

    override fun onCreate() {
        super.onCreate()

        db = Room.databaseBuilder(
            this,
            WeatherDatabase::class.java, "weather-db"
        ).build()
    }
}