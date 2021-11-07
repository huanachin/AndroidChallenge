package com.example.androidchallenge.ui.pages.splash

sealed class SplashEvent {
    object NavigateHome : SplashEvent()
    object NavigateLogin : SplashEvent()
}
