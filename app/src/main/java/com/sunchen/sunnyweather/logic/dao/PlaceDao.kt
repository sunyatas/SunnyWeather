package com.sunchen.sunnyweather.logic.dao

import android.content.Context.MODE_PRIVATE
import android.preference.PreferenceDataStore
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.sunchen.sunnyweather.MyApplication
import com.sunchen.sunnyweather.logic.model.Place
import androidx.core.content.edit

/**
 * @CreateTime 2025-04-16 09:42
 *
 * @Author sunchen
 *
 * @Description
 */

object PlaceDao {

    fun savePlace(place: Place) {
        getSharedPreference().edit {
            putString("PLACE", Gson().toJson(place))
        }

    }

    fun getPlace(): Place {
        val placeJson = getSharedPreference().getString("PLACE", "")
        try {
            return Gson().fromJson<Place>(placeJson, Place::class.java)
        } catch (e: JsonSyntaxException) {
            println(e.message)
            throw e
        }
    }

    fun isPlaceSaved(): Boolean {
        return getSharedPreference().contains("PLACE")
    }

    private fun getSharedPreference() =
        MyApplication.context.getSharedPreferences("WEATHER_SHARED_PREFERENCES", MODE_PRIVATE)

}