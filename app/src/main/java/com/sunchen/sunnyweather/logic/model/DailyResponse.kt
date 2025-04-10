package com.sunchen.sunnyweather.logic.model


import com.google.gson.annotations.SerializedName

data class DailyResponse(
    val result: Result,
    val status: String
) {
    data class Result(
        val daily: Daily
    ) {
        data class Daily(
            @SerializedName("life_index")
            val lifeIndex: LifeIndex,
            val skycon: List<Skycon>,
            val temperature: List<Temperature>
        ) {
            data class LifeIndex(
                val carWashing: List<CarWashing>,
                val coldRisk: List<ColdRisk>,
                val dressing: List<Dressing>,
                val ultraviolet: List<Ultraviolet>
            ) {
                data class CarWashing(
                    val desc: String
                )

                data class ColdRisk(
                    val desc: String
                )

                data class Dressing(
                    val desc: String
                )

                data class Ultraviolet(
                    val desc: String
                )
            }

            data class Skycon(
                val date: String,
                val value: String
            )

            data class Temperature(
                val max: Double,
                val min: Double
            )
        }
    }
}