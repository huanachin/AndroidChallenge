package com.example.androidchallenge.ui.pages.login

import com.example.androidchallenge.data.failure.LoginFailure

sealed class LoginEvent {
    data class NavigateHome(val userId: String) : LoginEvent()
    data class ShowError(val failure: LoginFailure) : LoginEvent()
}
