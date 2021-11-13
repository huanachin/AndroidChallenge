package com.example.androidchallenge.ui.util

import com.google.gson.Gson
import java.net.URLEncoder

fun <T> T.parseModel(): String {
    return Gson().toJson(this).encode()
}

fun String.encode(): String {
    return URLEncoder.encode(this, "utf-8")
}

inline fun <reified T> String.extractModel(): T? {
    return try {
        Gson().fromJson(this, T::class.java)
    } catch (e: Exception) {
        null
    }
}