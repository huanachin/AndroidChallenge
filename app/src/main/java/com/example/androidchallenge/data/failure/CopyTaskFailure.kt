package com.example.androidchallenge.data.failure

sealed class CopyTaskFailure {
    object TaskNotFound : CopyTaskFailure()
    object ServerFailure : CopyTaskFailure()
}