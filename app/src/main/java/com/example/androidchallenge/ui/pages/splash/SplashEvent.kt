package com.example.androidchallenge.ui.pages.splash

sealed class SplashEvent {
    data class NavigateHome(val userId: String) : SplashEvent()
    object NavigateLogin : SplashEvent()
}
