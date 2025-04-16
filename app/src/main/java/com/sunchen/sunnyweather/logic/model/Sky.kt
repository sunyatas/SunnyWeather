package com.sunchen.sunnyweather.logic.model

import com.sunchen.sunnyweather.R

/**
 * @CreateTime 2025-04-15 09:42
 *
 * @Author sunchen
 *
 * @Description
 */

data class Sky(val info: String, val icon: Int, val bg: Int)

private val sky = mapOf(
    "CLEAR_DAY" to Sky("晴", R.drawable.ic_clear_day, R.drawable.bg_clear_day),
    "CLEAR_NIGHT" to Sky("晴", R.drawable.ic_clear_night, R.drawable.bg_clear_night)
)


fun getSky(skycon: String): Sky {
    return sky[skycon] ?: sky["CLEAR_DAY"]!!
}


