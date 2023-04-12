package com.example.myweather.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myweather.databinding.ActivityMainBinding
import com.example.myweather.model.DayWeather
import com.example.myweather.model.WeatherRepository
import com.example.myweather.ui.detail.DetailActivity

class MainActivity : AppCompatActivity(), MainPresenter.View {

    private val presenter by lazy {
        MainPresenter(WeatherRepository(this), lifecycleScope)
    }

    private val adapter = WeatherListAdapter { presenter.onMovieClicked(it) }
    private lateinit var binding: ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter.onCreate(this@MainActivity)
        binding.rvWeather.adapter = adapter

    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun showProgress() {
        binding.progress.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding.progress.visibility = View.GONE
    }

    override fun updateData(weatherList: List<DayWeather>) {
        adapter.submitList(weatherList)
    }

    override fun navigateTo(dayWeather: DayWeather) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.DAY_WEATHER, dayWeather)
        startActivity(intent)
    }

    override fun setTitle (cityName: String) {
        // Ponemos el nombre de la ciudad en la barra de título
        val title = supportActionBar?.title
        supportActionBar?.title = "$title - $cityName"
    }

}
