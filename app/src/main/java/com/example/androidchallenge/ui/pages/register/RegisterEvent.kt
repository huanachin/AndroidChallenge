package com.example.androidchallenge.ui.pages.register

import com.example.androidchallenge.data.failure.RegisterFailure

sealed class RegisterEvent {
    object NavigateHome: RegisterEvent()
    data class ShowError(val failure: RegisterFailure) : RegisterEvent()
}
