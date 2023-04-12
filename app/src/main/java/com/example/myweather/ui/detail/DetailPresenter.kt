package com.example.myweather.ui.detail

import com.example.myweather.model.DayWeather

class DetailPresenter {

    private var view: View? = null

    interface View {
        fun updateUI(dayWeather: DayWeather)
    }

    fun onCreate(view: View, dayWeather: DayWeather) {
        this.view = view
        view.updateUI(dayWeather)
    }

    fun onDestroy() {
        view = null
    }
}
