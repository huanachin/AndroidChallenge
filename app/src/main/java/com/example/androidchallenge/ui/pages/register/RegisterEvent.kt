package com.example.androidchallenge.ui.pages.register

import com.example.androidchallenge.data.failure.RegisterFailure

sealed class RegisterEvent {
    data class NavigateHome(val userId: String) : RegisterEvent()
    data class ShowError(val failure: RegisterFailure) : RegisterEvent()
}
