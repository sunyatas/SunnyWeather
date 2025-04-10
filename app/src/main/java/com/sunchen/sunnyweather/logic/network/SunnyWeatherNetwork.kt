package com.sunchen.sunnyweather.logic.network

import com.sunchen.sunnyweather.logic.model.PlaceResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * @CreateTime 2025-04-09 15:45
 *
 * @Author sunchen
 *
 * @Description
 */

object SunnyWeatherNetwork {

    // 创建一个PlaceService的动态代理对象
    private val placeService = ServiceCreator.create<PlaceService>()
    private val weatherService = ServiceCreator.create<WeatherService>()

    // 调用searchPlaces，发起搜索城市请求
    // 当外部调用searchPlaces时，Retrofit会立即发起网络请求，同时当前协程也会被阻塞住，
    // 直到服务器响应请求await函数将解析出来的数据模型返回，同事恢复当前协程，
    // searchPlaces()函数得到await函数的返回值后会将该数据 再返回到上一层
    // Retrofit 2.6.0+ 原生支持 协程挂起函数。无需如下写法
    // suspend fun searchPlaces(query: String) = placeService.searchPlaces(query).await()
    suspend fun searchPlaces(query: String) = placeService.searchPlaces(query)

    suspend fun getRealTimeWeather(lng: String, lat: String) =
        weatherService.getRealtimeWeather(lng, lat)

    suspend fun getDailyTimeWeather(lng: String, lat: String) =
        weatherService.getDailyWeather(lng, lat)


    // private suspend fun <T> Call<T>.await(): T {
    //     return suspendCoroutine { continuation ->
    //         enqueue(object : Callback<T> {
    //             override fun onResponse(call: Call<T>, response: Response<T>) {
    //                 val body = response.body()
    //                 if (body != null) continuation.resume(body)
    //                 else continuation.resumeWithException(RuntimeException("response body is null"))
    //             }
    //             override fun onFailure(call: Call<T>, t: Throwable) {
    //                 continuation.resumeWithException(t)
    //             }
    //
    //         })
    //     }
    // }


}