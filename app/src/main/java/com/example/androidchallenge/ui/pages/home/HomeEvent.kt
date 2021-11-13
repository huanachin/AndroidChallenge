package com.example.androidchallenge.ui.pages.home

sealed class HomeEvent {
    object ShowDeleteTaskSuccess : HomeEvent()
    object ShowDeleteTaskError : HomeEvent()
    object ShowAddTaskSuccess : HomeEvent()
    object ShowAddTaskError : HomeEvent()
}