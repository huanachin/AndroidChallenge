package com.example.androidchallenge.data.failure

sealed class LoginFailure {
    object WrongEmailPasswordFailure : LoginFailure()
    object ServerFailure : LoginFailure()
}
