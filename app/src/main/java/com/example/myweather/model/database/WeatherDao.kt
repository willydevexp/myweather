package com.example.myweather.model.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Query("SELECT * FROM Weather")
    fun getAll(): Flow<List<Weather>>

    @Query("SELECT * FROM Weather WHERE dt = :dt")
    fun getWeather(dt: Int): Flow<Weather>

    @Query("SELECT COUNT(dt) FROM Weather")
    fun weatherCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeatherList(weather: List<Weather>)

    @Update
    fun updateWeather(weather: Weather)

}