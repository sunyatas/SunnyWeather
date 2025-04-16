package com.sunchen.sunnyweather.ui.weather

import android.app.assist.AssistContent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.sunchen.sunnyweather.R
import com.sunchen.sunnyweather.databinding.ActivityWeatherBinding
import com.sunchen.sunnyweather.databinding.ForecastBinding
import com.sunchen.sunnyweather.databinding.ForecastItemBinding
import com.sunchen.sunnyweather.databinding.LifeIndexBinding
import com.sunchen.sunnyweather.databinding.NowBinding
import com.sunchen.sunnyweather.logic.model.WeatherModel
import com.sunchen.sunnyweather.logic.model.getSky
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class WeatherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWeatherBinding
    private lateinit var nowBinding: NowBinding
    private lateinit var forecastBinding: ForecastBinding
    private lateinit var lifeIndexBinding: LifeIndexBinding
    private lateinit var viewModel: WeatherViewModel
    override fun onProvideAssistContent(outContent: AssistContent?) {
        super.onProvideAssistContent(outContent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWeatherBinding.inflate(layoutInflater)
        nowBinding = NowBinding.bind(binding.now.root)
        forecastBinding = ForecastBinding.bind(binding.forecast.root)
        lifeIndexBinding = LifeIndexBinding.bind(binding.lifeIndex.root)
        viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.weatherLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        WindowCompat.getInsetsController(window, window.decorView)
            .apply {
                isAppearanceLightStatusBars = false
            }

        if (viewModel.locationLng.isEmpty()) {
            viewModel.locationLng = intent.getStringExtra("location_lng") ?: ""
        }
        if (viewModel.locationLat.isEmpty()) {
            viewModel.locationLat = intent.getStringExtra("location_lat") ?: ""
        }
        if (viewModel.locationName.isEmpty()) {
            viewModel.locationName = intent.getStringExtra("location_name") ?: ""
        }

        Log.d(
            javaClass.name,
            "lng:${viewModel.locationLng},lat:${viewModel.locationLat},name:${viewModel.locationName}"
        )

        // 观察weatherData
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.weatherData.collect { result ->
                    result.onSuccess {
                        it?.let {
                            initView(it)
                        }
                        Log.d(javaClass.name, "weatherData:$it")
                    }
                    result.onFailure {
                        Toast.makeText(this@WeatherActivity, it.message, Toast.LENGTH_SHORT)
                            .show()
                    }

                }
            }
        }
        viewModel.refreshWeather(viewModel.locationLng ?: "", viewModel.locationLat ?: "")

    }

    private fun initView(weatherViewModel: WeatherModel) {
        nowBinding.placeName.text = viewModel.locationName
        val realtime = weatherViewModel.realtime
        val daily = weatherViewModel.daily
        // now.xml
        nowBinding.currentTemp.text = "${realtime.temperature} ℃"
        nowBinding.currentSky.text = "${getSky(realtime.skycon).info}"
        nowBinding.currentAQI.text = "空气指数：${realtime.airQuality.aqi.chn}"
        nowBinding.nowLayout.setBackgroundResource(getSky(realtime.skycon).bg)
        // forecast
        forecastBinding.forecastLayout.removeAllViews()
        val days = daily.skycon.size
        for (i in 0 until days) {
            val skycon = daily.skycon[i]
            val temperature = daily.temperature[i]
            val sky = getSky(skycon.value)
            val view = layoutInflater.inflate(
                R.layout.forecast_item, forecastBinding.forecastLayout, false
            )
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val parseDate = dateFormat.parse(skycon.date)
            val forecastItemBinding = ForecastItemBinding.bind(view)
            forecastItemBinding.skyIcon.setImageResource(sky.icon)
            forecastItemBinding.dateInfo.text =
                SimpleDateFormat("MM月dd", Locale.getDefault()).format(parseDate)
            forecastItemBinding.skyInfo.text = sky.info
            forecastItemBinding.temperatureInfo.text = "${temperature.min} ～ ${temperature.max}℃"
            forecastBinding.forecastLayout.addView(view)
        }
        // life_index
        val lifeIndex = daily.lifeIndex
        lifeIndexBinding.coldRiskText.text = lifeIndex.coldRisk[0].desc
        lifeIndexBinding.dressingText.text = lifeIndex.dressing[0].desc
        lifeIndexBinding.ultravioletText.text = lifeIndex.ultraviolet[0].desc
        lifeIndexBinding.carWashingText.text = lifeIndex.carWashing[0].desc
    }

}