package com.example.androidchallenge.ui.pages.task

sealed class TaskEvent {
    object AddTaskSuccess : TaskEvent()
    object AddTaskError : TaskEvent()

}