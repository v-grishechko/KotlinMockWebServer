package com.kotlinmockwebserver.utils

import java.io.File

object ResponseUtils {

    fun getFileAsString(path: String): String {
       return javaClass.classLoader.getResourceAsStream(path).bufferedReader().use { it.readText() }
    }
}