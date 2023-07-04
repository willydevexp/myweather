package com.example.myweather.di

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.room.Room.databaseBuilder
import com.example.myweather.R
import com.example.myweather.data.PermissionChecker
import com.example.myweather.data.datasource.LocalDataSource
import com.example.myweather.data.datasource.LocationServiceDataSource
import com.example.myweather.data.datasource.WeatherRemoteDataSource
import com.example.myweather.framework.AndroidPermissionChecker
import com.example.myweather.framework.PlayServicesLocationDataSource
import com.example.myweather.framework.database.AppDatabase
import com.example.myweather.framework.database.RoomDataSource
import com.example.myweather.framework.server.RemoteService
import com.example.myweather.framework.server.WeatherServerDataSource
import com.example.myweather.ui.weather.WeatherFragmentArgs
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    @ApiKey
    fun provideApiKey(app: Application): String = app.getString(R.string.api_key)

    @Provides
    @Singleton
    fun provideAppDatabase(app: Application) = databaseBuilder(
        app,
        AppDatabase::class.java,
        "MyWeather-db"
    ).build()

    @Provides
    @Singleton
    fun provideAppDao(db: AppDatabase) = db.appDao()

    @Provides
    @Singleton
    @ApiUrl
    fun provideApiUrl(): String = "https://api.openweathermap.org/"

    @Provides
    @ViewModelScoped
    @LocationId
    fun provideLocationId(savedStateHandle: SavedStateHandle) =
        WeatherFragmentArgs.fromSavedStateHandle(savedStateHandle).locationId

    @Provides
    @Singleton
    fun provideRemoteService(@ApiUrl apiUrl : String): RemoteService {
        val okHttpClient = HttpLoggingInterceptor().run {
            level = HttpLoggingInterceptor.Level.BODY
            OkHttpClient.Builder().addInterceptor(this).build()
        }

        return Retrofit.Builder()
            .baseUrl(apiUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create()
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class AppDataModule {

    @Binds
    abstract fun bindLocalDataSource(localDataSource: RoomDataSource): LocalDataSource

    @Binds
    abstract fun bindLocationServiceDataSource(locationDataSource: PlayServicesLocationDataSource): LocationServiceDataSource

    @Binds
    abstract fun bindRemoteDataSource(remoteDataSource: WeatherServerDataSource): WeatherRemoteDataSource

    @Binds
    abstract fun bindPermissionChecker(permissionChecker: AndroidPermissionChecker): PermissionChecker

}