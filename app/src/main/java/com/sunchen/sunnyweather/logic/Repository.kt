package com.sunchen.sunnyweather.logic

import androidx.lifecycle.liveData
import com.sunchen.sunnyweather.logic.model.Place
import com.sunchen.sunnyweather.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.http.Query

/**
 * @CreateTime 2025-04-09 16:10
 *
 * @Author sunchen
 *
 * @Description 仓库层的主要作用是判断调用方请求的数据应该是从本地数据源还是从网络数据源中获取，并将获得的数据返回给调用方
 */

object Repository {

    fun searchPlaces(query: String) = liveData(Dispatchers.IO) {
        val result = try {
            val searchPlaces = SunnyWeatherNetwork.searchPlaces(query)
            if (searchPlaces.status == "ok") {
                val places = searchPlaces.places
                Result.success(places)
            } else {
                Result.failure(RuntimeException("响应状态：${searchPlaces.status}"))
            }
        } catch (e: Exception) {
            Result.failure<List<Place>>(e)
        }
        emit(result)
    }

    fun searchPlacesFlow(placeName: String): Flow<Result<List<Place>>> {
        return flow {
            val result = try {
                val searchPlaces = SunnyWeatherNetwork.searchPlaces(placeName)
                if (searchPlaces.status == "ok") {
                    val places = searchPlaces.places
                    Result.success(places)
                } else {
                    Result.failure(RuntimeException("响应状态：${searchPlaces.status}"))
                }
            } catch (e: Exception) {
                Result.failure<List<Place>>(e)
            }
            emit(result)
        }.flowOn(Dispatchers.IO)


    }
}