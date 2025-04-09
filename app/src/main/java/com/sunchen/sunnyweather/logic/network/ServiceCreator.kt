package com.sunchen.sunnyweather.logic.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @CreateTime 2025-04-09 15:25
 *
 * @Author sunchen
 *
 * @Description
 */
// https://api.caiyunapp.com/v2/place?query=上海&token=vmiSIFWhdBmIbj0i&lang=zh_CN
object ServiceCreator {
    private const val BASE_URL = "https://api.caiyunapp.com/"

    private val retrofit =
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
            .build()

    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)

    inline fun <reified T> create(): T = create(T::class.java)
}