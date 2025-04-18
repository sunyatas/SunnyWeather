package com.sunchen.sunnyweather.ui.weather

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sunchen.sunnyweather.logic.Repository
import com.sunchen.sunnyweather.logic.model.Location
import com.sunchen.sunnyweather.util.TAG
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * @CreateTime 2025-04-14 15:45
 *
 * @Author sunchen
 *
 * @Description
 */
@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModel : ViewModel() {
    var locationLng: String = ""
    var locationLat: String = ""
    var locationName: String = ""
    private val locationFlow = MutableSharedFlow<Location>(replay = 1)

    val weatherData = locationFlow.flatMapLatest {
        Repository.refreshWeather(it.lng, it.lat)
    }
        .shareIn(
            viewModelScope, kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(3000),
            1
        )


    fun refreshWeather(lng: String, lat: String) {
        Log.d(TAG, "刷新")
        viewModelScope.launch {
            locationFlow.emit(Location(lng, lat))
        }
    }
}