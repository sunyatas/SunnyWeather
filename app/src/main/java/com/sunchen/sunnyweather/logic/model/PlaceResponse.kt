package com.sunchen.sunnyweather.logic.model

import com.google.gson.annotations.SerializedName

/**
 * @CreateTime 2025-04-09 14:53
 *
 * @Author sunchen
 *
 * @Description
 */

data class PlaceResponse(val status: String, val places: List<Place>)

data class Place(
    val name: String,
    val location: Location,
    @SerializedName("formatted_address") val address: String
)

data class Location(val lng: String, val lat: String)