package com.sunchen.sunnyweather.logic

import android.util.Log
import androidx.lifecycle.liveData
import com.sunchen.sunnyweather.logic.dao.PlaceDao
import com.sunchen.sunnyweather.logic.model.Place
import com.sunchen.sunnyweather.logic.model.WeatherModel
import com.sunchen.sunnyweather.logic.network.SunnyWeatherNetwork
import com.sunchen.sunnyweather.util.TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import retrofit2.http.Query
import kotlin.coroutines.CoroutineContext

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

    fun searchPlacesFlow(placeName: String) = fire {
        val searchPlaces = SunnyWeatherNetwork.searchPlaces(placeName)
        if (searchPlaces.status == "ok") {
            val places = searchPlaces.places
            Result.success(places)
        } else {
            Result.failure(RuntimeException("响应错误，状态：${searchPlaces.status}"))
        }
    }

    fun refreshWeather(lng: String, lat: String) = fire {
        val (realTimeWeather, dailyWeather) = coroutineScope {
            Log.d(TAG, "请求天气数据")
            val deferredTimeWeather = async { SunnyWeatherNetwork.getRealTimeWeather(lng, lat) }
            val deferredDailyWeather = async { SunnyWeatherNetwork.getDailyTimeWeather(lng, lat) }
            val realTimeWeather = deferredTimeWeather.await()
            val dailyWeather = deferredDailyWeather.await()
            Pair(realTimeWeather, dailyWeather)
        }

        if (realTimeWeather.status == "ok" && dailyWeather.status == "ok") {
            val weatherModel = WeatherModel(
                realTimeWeather.result.realtime, dailyWeather.result.daily
            )
            Result.success(weatherModel)
        } else {
            Result.failure(RuntimeException("API 响应错误: 实时天气状态=${realTimeWeather.status}, 每日天气状态=${dailyWeather.status}"))
        }
    }


    // 封装flow处理try/catch
    private fun <T> fire(
        coroutine: CoroutineContext = Dispatchers.IO, block: suspend () -> Result<T>
    ): Flow<Result<T>> {
        return flow<Result<T>> {
            val result = try {
                block()
            } catch (e: Exception) {
                Result.failure<T>(e)
            }
            emit(result)
        }.flowOn(coroutine)
    }

    suspend fun savePlace(place: Place) {
        withContext(Dispatchers.IO) {
            PlaceDao.savePlace(place)
        }

    }

    fun getPlace() = flow<Place> {
        val place = withContext(Dispatchers.IO) {
            PlaceDao.getPlace()
        }
        emit(place)
    }


    fun isPlaceSaved(): Boolean {
        return PlaceDao.isPlaceSaved()
    }

}





