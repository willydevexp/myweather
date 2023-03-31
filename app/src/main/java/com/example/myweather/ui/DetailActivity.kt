package com.example.myweather.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import com.example.myweather.databinding.ActivityDetailBinding
import com.example.myweather.getDate
import com.example.myweather.loadUrl
import com.example.myweather.model.DayWeather
import java.util.*
import kotlin.math.roundToInt

class DetailActivity : AppCompatActivity() {
    companion object {
        const val WEATHER = "DetailActivity:weather"
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityDetailBinding.inflate(layoutInflater).run {
            setContentView(root)

            val dayWeather = intent.getParcelableExtra<DayWeather>(WEATHER) ?: throw IllegalStateException()

            weatherDetailToolbar.title = ""
            weatherDetailImage.loadUrl("https://openweathermap.org/img/wn/${dayWeather.weather[0].icon}@4x.png")
            weatherDetailSummary.text = getDate(dayWeather.dt)
            weatherDetailInfo.setWeatherInfo(dayWeather)

        }

    }
}