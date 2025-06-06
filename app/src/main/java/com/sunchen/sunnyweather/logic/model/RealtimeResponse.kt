package com.sunchen.sunnyweather.logic.model


import com.google.gson.annotations.SerializedName

data class RealtimeResponse(
    @SerializedName("api_status")
    val apiStatus: String,
    @SerializedName("api_version")
    val apiVersion: String,
    val lang: String,
    val location: List<Double>,
    val result: Result,
    @SerializedName("server_time")
    val serverTime: Int,
    val status: String,
    val timezone: String,
    val tzshift: Int,
    val unit: String
) {
    data class Result(
        val primary: Int,
        val realtime: Realtime
    ) {
        data class Realtime(
            @SerializedName("air_quality")
            val airQuality: AirQuality,
            @SerializedName("apparent_temperature")
            val apparentTemperature: Double,
            val cloudrate: Double,
            val dswrf: Double,
            val humidity: Double,
            @SerializedName("life_index")
            val lifeIndex: LifeIndex,
            val precipitation: Precipitation,
            val pressure: Double,
            val skycon: String,
            val status: String,
            val temperature: Double,
            val visibility: Double,
            val wind: Wind
        ) {
            data class AirQuality(
                val aqi: Aqi,
                val co: Double,
                val description: Description,
                val no2: Int,
                val o3: Int,
                val pm10: Int,
                val pm25: Int,
                val so2: Int
            ) {
                data class Aqi(
                    val chn: Int,
                    val usa: Int
                )

                data class Description(
                    val chn: String,
                    val usa: String
                )
            }

            data class LifeIndex(
                val comfort: Comfort,
                val ultraviolet: Ultraviolet
            ) {
                data class Comfort(
                    val desc: String,
                    val index: Int
                )

                data class Ultraviolet(
                    val desc: String,
                    val index: Double
                )
            }

            data class Precipitation(
                val local: Local,
                val nearest: Nearest
            ) {
                data class Local(
                    val datasource: String,
                    val intensity: Double,
                    val status: String
                )

                data class Nearest(
                    val distance: Double,
                    val intensity: Double,
                    val status: String
                )
            }

            data class Wind(
                val direction: Double,
                val speed: Double
            )
        }
    }
}