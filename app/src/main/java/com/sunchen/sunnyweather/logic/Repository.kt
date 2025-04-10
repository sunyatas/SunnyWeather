package com.sunchen.sunnyweather.logic

import androidx.lifecycle.liveData
import com.sunchen.sunnyweather.logic.model.Place
import com.sunchen.sunnyweather.logic.model.WeatherModel
import com.sunchen.sunnyweather.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.runBlocking
import retrofit2.http.Query

/**
 * @CreateTime 2025-04-09 16:10
 *
 * @Author sunchen
 *
 * @Description 仓库层的主要作用是判断调用方请求的数据应该是从本地数据源还是从网络数据源中获取，并将获得的数据返回给调用方
 */

object Repository {

    // livedata函数自动构建并返回一个LiveData对象，然后在它的代码块中提供一个挂起函数的上下文
    // fun searchPlaces(query: String) = liveData(Dispatchers.IO) {
    //     val result = try {
    //         val searchPlaces = SunnyWeatherNetwork.searchPlaces(query)
    //         if (searchPlaces.status == "ok") {
    //             val places = searchPlaces.places
    //             Result.success(places)
    //         } else {
    //             Result.failure(RuntimeException("响应状态：${searchPlaces.status}"))
    //         }
    //     } catch (e: Exception) {
    //         Result.failure<List<Place>>(e)
    //     }
    //     emit(result)
    // }

    fun searchPlacesFlow(placeName: String): Flow<Result<List<Place>>> {
        return flow {
            val result = try {
                val searchPlaces = SunnyWeatherNetwork.searchPlaces(placeName)
                if (searchPlaces.status == "ok") {
                    val places = searchPlaces.places
                    Result.success(places)
                } else {
                    Result.failure(RuntimeException("响应错误，状态：${searchPlaces.status}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
            emit(result)
        }.flowOn(Dispatchers.IO)
    }


    fun searchPlaces2(query: String): Flow<Result<List<Place>>> = flow {
        val response = try {
            SunnyWeatherNetwork.searchPlaces(query) // 挂起函数调用
        } catch (e: Exception) {
            emit(Result.failure(e)) // 网络异常
            return@flow
        }
        if (response.status == "ok") {
            emit(Result.success(response.places ?: emptyList())) // 处理空数据
        } else {
            emit(Result.failure(RuntimeException("响应状态：${response.status}")))
        }
    }.flowOn(Dispatchers.IO) // 确保整个 Flow 在 IO 线程执行


    fun refreshWeather(lng: String, lat: String) {

        val flow: Flow<Result<WeatherModel>> = flow {
            val result = try {
                coroutineScope {
                    val deferredRealtime = async {
                        SunnyWeatherNetwork.getRealTimeWeather(lng, lat)
                    }
                    val deferredDaily = async {
                        SunnyWeatherNetwork.getDailyTimeWeather(lng, lat)
                    }
                    val realtimeResponse = deferredRealtime.await()
                    val dailyResponse = deferredDaily.await()
                    if (realtimeResponse.status == "ok" && dailyResponse.status == "ok") {
                        val weather = WeatherModel(
                            realtimeResponse.result.realtime, dailyResponse.result.daily
                        )
                        Result.success(weather)
                    } else {
                        Result.failure(
                            RuntimeException(
                                "realtime response status is ${realtimeResponse.status} " + "daily response status is ${dailyResponse.status}"
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                Result.failure<WeatherModel>(e)
            }
            emit(result)
        }.flowOn(Dispatchers.IO)

        // val ss = flow {
        //     emit(SunnyWeatherNetwork.getRealTimeWeather(lng, lat))
        // }
        // val xx = flow { emit(SunnyWeatherNetwork.getDailyTimeWeather(lng, lat)) }
        //
        // ss.zip(xx) { realtimeResponse, dailyResponse ->
        //     WeatherModel(realtimeResponse.result.realtime, dailyResponse.result.daily)
        // }
    }


}



