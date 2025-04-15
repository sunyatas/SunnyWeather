package com.sunchen.sunnyweather.logic

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

/**
 * @CreateTime 2025-04-11 10:16
 *
 * @Author sunchen
 *
 * @Description
 */

fun main() {
    runBlocking {
        testAsync()
    }
}

suspend fun testContext() {

    val startTime = System.currentTimeMillis()
    coroutineScope {
        val with1 = withContext(Dispatchers.IO) {
            delay(1000)
            println("withContext1")
            5 + 5
        }
        val with2 = withContext(Dispatchers.IO) {
            delay(1000)
            println("withContext2")
            10 + 10
        }
        println("with1 + with2 = ${with1 + with2}")
        val endTime = System.currentTimeMillis()
        println("耗时:${endTime - startTime}")
    }


}

suspend fun testAsync() {
    val startTime = System.currentTimeMillis()

    coroutineScope {
        val async1 = async {
            delay(1000)
            println("async1")
            5 + 5
        }

        val async2 = async {
            delay(1000)
            println("async2")
            10 + 10
        }

        println("async1 + async2 = ${async1.await() + async2.await()}")
        val endTime = System.currentTimeMillis()
        println("耗时:${endTime - startTime}")

    }


}