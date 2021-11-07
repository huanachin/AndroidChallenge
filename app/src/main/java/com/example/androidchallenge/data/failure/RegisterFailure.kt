package com.example.androidchallenge.data.failure

sealed class RegisterFailure {
    object WrongEmailPasswordFailure : RegisterFailure()
    object ServerFailure : RegisterFailure()
}
