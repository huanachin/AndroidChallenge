package com.example.androidchallenge.core

sealed class ResultType<out T, out V> {
    data class Success<T, V>(val data: T) : ResultType<T, V>()
    data class Error<T, V>(val error: V) : ResultType<T, V>()
}