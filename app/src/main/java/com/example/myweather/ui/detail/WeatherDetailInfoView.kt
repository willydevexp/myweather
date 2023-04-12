package com.example.myweather.ui.detail

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import com.example.myweather.model.DayWeather
import kotlin.math.roundToInt

class WeatherDetailInfoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    fun setWeatherInfo(dayWeather: DayWeather) = with(dayWeather) {
        text = buildSpannedString {

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
