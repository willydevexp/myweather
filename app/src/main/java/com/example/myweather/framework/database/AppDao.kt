package com.example.myweather.framework.database


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {

    @Query("SELECT * FROM EntityWeather")
    fun getAllWeather(): Flow<List<EntityWeather>>

    @Query("SELECT * FROM EntityWeather WHERE locationId=:locationId ORDER BY dt")
    fun getWeatherOfLocation(locationId: Int): Flow<List<EntityWeather>>

    @Query("SELECT * FROM EntityWeather WHERE dt = :dt")
    fun getWeather(dt: Int): Flow<EntityWeather>

    @Query("SELECT COUNT(dt) FROM EntityWeather")
    suspend fun weatherCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeatherList(weatherList: List<EntityWeather>)

    @Query("DELETE FROM EntityWeather WHERE locationId=:locationId")
    suspend fun deleteWeatherOfLocation(locationId: Int)

    @Query("SELECT COUNT(id) FROM EntityLocation")
    suspend fun locationCount(): Int

    @Query("SELECT * FROM EntityLocation WHERE id!=-1")
    fun getLocationList(): Flow<List<EntityLocation>>

    @Query("SELECT * FROM EntityLocation WHERE id=:id")
    suspend fun getLocation(id: Int): EntityLocation

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(location: EntityLocation)

    @Query("DELETE FROM EntityLocation WHERE id=:id")
    suspend fun deleteLocation(id: Int)

}
