package com.example.androidchallenge.ui.pages.login

import com.example.androidchallenge.data.failure.LoginFailure

sealed class LoginEvent {
    object NavigateHome : LoginEvent()
    data class ShowError(val failure: LoginFailure) : LoginEvent()
}
