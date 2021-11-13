package com.example.androidchallenge.ui.pages.task

sealed class TaskEvent {
    object NavigateBack : TaskEvent()
    object ShowError : TaskEvent()
}