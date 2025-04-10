package com.sunchen.sunnyweather.logic.network

import com.sunchen.sunnyweather.MyApplication
import com.sunchen.sunnyweather.logic.model.DailyResponse
import com.sunchen.sunnyweather.logic.model.RealtimeResponse
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * @CreateTime 2025-04-10 16:48
 *
 * @Author sunchen
 *
 * @Description
 */

interface WeatherService {

    // https://api.caiyunapp.com/v2.5/vmiSIFWhdBmIbj0i/121.397516,31.626946/realtime
    @GET("/v2.5/${MyApplication.TOKEN}/{lng},{lat}/realtime")
    suspend fun getRealtimeWeather(
        @Path("lng") lng: String, @Path("lat") lat: String
    ): RealtimeResponse

    // https://api.caiyunapp.com/v2.5/vmiSIFWhdBmIbj0i/121.397516,31.626946/daily
    @GET("/v2.5/${MyApplication.TOKEN}/{lng},{lat}/daily")
    suspend fun getDailyWeather(@Path("lng") lng: String, @Path("lat") lat: String): DailyResponse
}