package com.example.myweather.ui.detail

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myweather.common.getDate
import com.example.myweather.common.loadUrl
import com.example.myweather.databinding.ActivityDetailBinding
import com.example.myweather.model.DayWeather
import java.util.*

class DetailActivity : AppCompatActivity(), DetailPresenter.View {
    companion object {
        const val DAY_WEATHER = "DetailActivity:dayWeather"
    }

    private val presenter = DetailPresenter()
    private lateinit var binding: ActivityDetailBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dayWeather: DayWeather = requireNotNull(intent.getParcelableExtra(DAY_WEATHER))
        presenter.onCreate(this, dayWeather)

    }


    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun updateUI(dayWeather: DayWeather) = with(binding) {
        weatherDetailToolbar.title = ""
        weatherDetailImage.loadUrl("https://openweathermap.org/img/wn/${dayWeather.weather[0].icon}@4x.png")
        weatherDetailSummary.text = getDate(dayWeather.dt)
        weatherDetailInfo.setWeatherInfo(dayWeather)
    }

}
