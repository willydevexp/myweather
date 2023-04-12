package com.example.myweather.ui.main

import com.example.myweather.model.DayWeather
import com.example.myweather.model.WeatherRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainPresenter(
    private val weatherRepository: WeatherRepository,
    private val scope: CoroutineScope
) {

    interface View {
        fun showProgress()
        fun hideProgress()
        fun updateData(weatherList: List<DayWeather>)
        fun navigateTo(dayWeather: DayWeather)
        fun setTitle(cityName: String)
    }

    private var view: View? = null

    fun onCreate(view: View) {
        this.view = view

        scope.launch {
            view.showProgress()
            view.updateData(weatherRepository.getDailyWeather()!!.list)
            view.hideProgress()
            view.setTitle(weatherRepository.getDailyWeather()!!.city.name)
        }
    }

    fun onMovieClicked(dayWeather: DayWeather) {
        view?.navigateTo(dayWeather)
    }

    fun onDestroy() {
        this.view = null
    }
}
