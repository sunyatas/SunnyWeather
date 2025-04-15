package com.sunchen.sunnyweather.ui.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sunchen.sunnyweather.logic.Repository
import com.sunchen.sunnyweather.logic.model.Location
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn

/**
 * @CreateTime 2025-04-14 15:45
 *
 * @Author sunchen
 *
 * @Description
 */
@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModel() : ViewModel() {
    private val locationFlow = MutableStateFlow<Location>(Location("", ""))

    val weatherData = locationFlow.flatMapLatest {
        Repository.refreshWeather(it.lng, it.lat)
    }
        .stateIn(
            viewModelScope,
            kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(3000),
            Result.success(null)
        )

    fun refreshWeather(lng: String, lat: String) {
        locationFlow.value = Location(lng, lat)
    }
}