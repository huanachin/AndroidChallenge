package com.example.androidchallenge.ui.pages.task

import android.net.Uri

sealed class TaskImage {
    data class TaskImageNetwork(val url: String, val path: String) : TaskImage()
    data class TaskImageLocal(val uri: Uri) : TaskImage()
}
