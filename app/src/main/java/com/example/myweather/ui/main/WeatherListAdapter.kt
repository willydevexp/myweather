package com.example.myweather.ui.main

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myweather.R
import com.example.myweather.common.basicDiffUtil
import com.example.myweather.common.getDate
import com.example.myweather.common.inflate
import com.example.myweather.common.loadUrl
import com.example.myweather.databinding.ItemWeatherBinding
import com.example.myweather.model.DayWeather
import kotlin.math.roundToInt

class WeatherListAdapter(private val listener: (DayWeather) -> Unit) :
    ListAdapter<DayWeather, WeatherListAdapter.ViewHolder>(basicDiffUtil { old, new -> old.dt == new.dt }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.inflate(R.layout.item_weather, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dayWeather = getItem(position)
        holder.bind(dayWeather)
        holder.itemView.setOnClickListener { listener(dayWeather) }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemWeatherBinding.bind(view)
        @SuppressLint("SetTextI18n")
        fun bind(dayWeather: DayWeather) = with(binding) {
            txtTemperature.text = "${dayWeather.temp.max.roundToInt()} / ${dayWeather.temp.min.roundToInt()} ºC"
            txtDescription.text = dayWeather.weather[0].description.uppercase()
            txtDay.text =  getDate(dayWeather.dt)
            imgWeather.loadUrl("https://openweathermap.org/img/wn/${dayWeather.weather[0].icon}.png")
        }
    }
}