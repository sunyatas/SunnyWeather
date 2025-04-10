package com.sunchen.sunnyweather.logic.network

import com.sunchen.sunnyweather.MyApplication
import com.sunchen.sunnyweather.logic.model.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @CreateTime 2025-04-09 14:51
 *
 * @Author sunchen
 *
 * @Description
 */

interface PlaceService {
    // https://api.caiyunapp.com/v2/place?query=上海&token=vmiSIFWhdBmIbj0i&lang=zh_CN
    @GET("v2/place?token=${MyApplication.TOKEN}&lang=zh_CN")
    // Retrofit 2.6.0+ 原生支持 协程挂起函数。无需如下写法
    // fun searchPlaces(@Query("query") query: String): Call<PlaceResponse>
    suspend fun searchPlaces(@Query("query") query: String): PlaceResponse

}