package com.example.myweather.ui

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myweather.R
import com.example.myweather.databinding.ItemWeatherBinding
import com.example.myweather.getDate
import com.example.myweather.inflate
import com.example.myweather.loadUrl
import com.example.myweather.model.DayWeather
import kotlin.math.roundToInt
import kotlin.properties.Delegates

class WeatherListAdapter(private val listener: (DayWeather) -> Unit) :
    RecyclerView.Adapter<WeatherListAdapter.ViewHolder>() {

    var weatherList: List<DayWeather> by Delegates.observable(emptyList()) { _, old, new ->
        DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                old[oldItemPosition].dt == new[newItemPosition].dt

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                old[oldItemPosition] == new[newItemPosition]

            override fun getOldListSize(): Int = old.size

            override fun getNewListSize(): Int = new.size
        }).dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.inflate(R.layout.item_weather, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = weatherList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val weather = weatherList[position]
        holder.bind(weather)
        holder.itemView.setOnClickListener { listener(weather) }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemWeatherBinding.bind(view)
        fun bind(weather: DayWeather) = with(binding) {
            txtTemperature.text = "${weather.temp.max.roundToInt()} / ${weather.temp.min.roundToInt()} ºC"
            txtDescription.text = weather.weather[0].description.uppercase()
            txtDay.text =  getDate(weather.dt)
            imgWeather.loadUrl("https://openweathermap.org/img/wn/${weather.weather[0].icon}.png")
        }
    }
}