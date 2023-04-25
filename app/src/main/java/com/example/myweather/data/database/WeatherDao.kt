package com.example.myweather.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Query("SELECT * FROM Weather")
    fun getAll(): Flow<List<Weather>>

    @Query("SELECT * FROM Weather WHERE dt = :dt")
    fun getWeather(dt: Int): Flow<Weather>

    @Query("SELECT COUNT(dt) FROM Weather")
    suspend fun weatherCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeatherList(weather: List<Weather>)

    @Update
    suspend fun updateWeather(weather: Weather)

    @Query("DELETE FROM Weather")
    suspend fun deleteAll()

    @Delete
    suspend fun deleteWeather(weather: Weather)


}