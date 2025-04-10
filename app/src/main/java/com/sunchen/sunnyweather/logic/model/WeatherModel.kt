package com.sunchen.sunnyweather.logic.model

/**
 * @CreateTime 2025-04-10 16:32
 *
 * @Author sunchen
 *
 * @Description
 */

data class WeatherModel(
    val realtime: RealtimeResponse.Result.Realtime, val daily: DailyResponse.Result.Daily
)

