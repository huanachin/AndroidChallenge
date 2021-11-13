package com.example.androidchallenge.ui.util

import com.google.gson.Gson
import java.net.URLEncoder

fun <T> T.parseModel(): String {
    return URLEncoder.encode(Gson().toJson(this), "utf-8")
}

inline fun <reified T> String.extractModel(): T {
    return Gson().fromJson(this, T::class.java)
}