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
        val binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.getParcelableExtra<DayWeather>(WEATHER)?.run {
            binding.weatherDetailToolbar.title = ""

            binding.weatherDetailImage.loadUrl("https://openweathermap.org/img/wn/${weather[0].icon}@4x.png")

            binding.weatherDetailSummary.text = getDate(dt)

            binding.weatherDetailInfo.text = buildSpannedString {

                bold { appendLine(weather[0].description.uppercase()) }

                bold { append("Temperature: ") }
                appendLine("${temp.max.roundToInt()} / ${temp.min.roundToInt()} ºC" )

                bold { append("Humidity: ") }
                appendLine("${humidity.toString()} %")

                bold { append("Pressure: ") }
                appendLine("${pressure.toString()} hPa")

                bold { append("Wind speed: ") }
                appendLine("${speed.toString()} Km/h")

            }
        }
    }
}