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
import com.example.myweather.model.database.Weather
import kotlin.math.roundToInt

class WeatherListAdapter(private val listener: (Weather) -> Unit) :
    ListAdapter<Weather, WeatherListAdapter.ViewHolder>(basicDiffUtil { old, new -> old.dt == new.dt }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.inflate(R.layout.item_weather, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val weather = getItem(position)
        holder.bind(weather)
        holder.itemView.setOnClickListener { listener(weather) }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemWeatherBinding.bind(view)
        @SuppressLint("SetTextI18n")
        fun bind(weather: Weather) = with(binding) {
            txtTemperature.text = "${weather.tempMax.roundToInt()} / ${weather.tempMin.roundToInt()} ºC"
            txtDescription.text = weather.description.uppercase()
            txtDay.text =  getDate(weather.dt)
            imgWeather.loadUrl("https://openweathermap.org/img/wn/${weather.icon}.png")
        }
    }
}
