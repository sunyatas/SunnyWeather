package com.sunchen.sunnyweather

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

/**
 * @CreateTime 2025-04-09 14:46
 *
 * @Author sunchen
 *
 * @Description
 */


class MyApplication : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        const val TOKEN = "vmiSIFWhdBmIbj0i"
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}