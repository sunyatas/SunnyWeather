package com.sunchen.sunnyweather.util

import android.content.Context
import android.util.TypedValue
import com.google.android.material.color.MaterialColors

/**
 * @CreateTime 2025-04-16 11:41
 *
 * @Author sunchen
 *
 * @Description
 */

fun Context.getColorFromAttr(attr: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(attr, typedValue, true)
    return typedValue.data

}
