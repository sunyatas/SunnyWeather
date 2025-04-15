package com.sunchen.sunnyweather.ui.weather

import android.app.assist.AssistContent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.sunchen.sunnyweather.R
import com.sunchen.sunnyweather.databinding.ActivityWeatherBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class WeatherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWeatherBinding
    private lateinit var viewModel: WeatherViewModel
    override fun onProvideAssistContent(outContent: AssistContent?) {
        super.onProvideAssistContent(outContent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.drawerLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val locationLng = intent.getStringExtra("location_lng")
        val locationLat = intent.getStringExtra("location_lat")
        val locationName = intent.getStringExtra("location_name")

        Log.d(javaClass.name, "lng:$locationLng,lat:$locationLat,name:$locationName")

        lifecycleScope.launch {
            viewModel.weatherData.collect {
                Log.d(javaClass.name, "weatherData:$it")
            }
        }

        viewModel.refreshWeather(locationLng ?: "", locationLat ?: "")
    }
}