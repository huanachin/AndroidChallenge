package com.example.androidchallenge.data.failure

sealed class AddElementFailure {
    object UnExpectedFailure : AddElementFailure()
}