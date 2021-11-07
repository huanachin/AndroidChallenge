package com.example.androidchallenge.data.failure

sealed class CurrentUserFailure {
    object UserNotFound : CurrentUserFailure()
    object ServerFailure : CurrentUserFailure()
}
