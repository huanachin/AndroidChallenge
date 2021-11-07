package com.example.androidchallenge.data.repository.interfaces

import com.example.androidchallenge.core.ResultType
import com.example.androidchallenge.data.failure.CurrentUserFailure
import com.example.androidchallenge.data.failure.LoginFailure
import com.example.androidchallenge.data.failure.RegisterFailure
import com.example.androidchallenge.data.model.UserModel

interface UserRepository {
    suspend fun authenticate(
        username: String,
        password: String
    ): ResultType<UserModel, LoginFailure>

    suspend fun register(username: String, password: String): ResultType<UserModel, RegisterFailure>

    suspend fun getCurrentUser(): ResultType<UserModel, CurrentUserFailure>

    fun logout()
}