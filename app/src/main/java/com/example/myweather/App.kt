package com.example.myweather

import android.app.Application
import androidx.room.Room
import com.example.myweather.framework.database.AppDatabase

class App : Application() {

    lateinit var db: AppDatabase
        private set

    override fun onCreate() {
        super.onCreate()

        db = Room.databaseBuilder(
            this,
            AppDatabase::class.java, "weather-db"
        ).build()
    }
}
