package com.example.androidchallenge.ui.pages.splash

import com.example.androidchallenge.data.model.TaskModel

sealed class SplashEvent {
    data class NavigateHome(val userId: String, val params: String?) : SplashEvent()
    object NavigateLogin : SplashEvent()
}
