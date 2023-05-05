package com.example.myweather.framework.database


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Query("SELECT * FROM WeatherTable")
    fun getAll(): Flow<List<WeatherTable>>

    @Query("SELECT * FROM WeatherTable WHERE dt = :dt")
    fun getWeather(dt: Int): Flow<WeatherTable>

    @Query("SELECT COUNT(dt) FROM WeatherTable")
    suspend fun weatherCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeatherList(weatherTable: List<WeatherTable>)

    @Update
    suspend fun updateWeather(weatherTable: WeatherTable)

    @Query("DELETE FROM WeatherTable")
    suspend fun deleteAll()

    @Delete
    suspend fun deleteWeather(weatherTable: WeatherTable)


}
