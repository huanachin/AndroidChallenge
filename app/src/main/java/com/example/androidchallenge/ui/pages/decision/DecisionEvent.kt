package com.example.androidchallenge.ui.pages.decision

sealed class DecisionEvent {
    data class NavigateHome(val userId: String) : DecisionEvent()
    object NavigateLogin : DecisionEvent()
}
