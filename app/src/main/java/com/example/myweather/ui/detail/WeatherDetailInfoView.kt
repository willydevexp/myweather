package com.example.myweather.ui.detail

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import com.example.myweather.data.database.Weather
import kotlin.math.roundToInt

class WeatherDetailInfoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    fun setWeatherInfo(weather: Weather) = with(weather) {
        text = buildSpannedString {

            bold { appendLine(description.uppercase()) }

            bold { append("Temperature: ") }
            appendLine("${tempMax.roundToInt()} / ${tempMin.roundToInt()} ºC" )

            bold { append("Humidity: ") }
            appendLine("${humidity.toString()} %")

            bold { append("Pressure: ") }
            appendLine("${pressure.toString()} hPa")

            bold { append("Wind speed: ") }
            appendLine("${speed.toString()} Km/h")

        }
    }

}
