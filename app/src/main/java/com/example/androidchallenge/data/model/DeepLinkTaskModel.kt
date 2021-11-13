package com.example.androidchallenge.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DeepLinkTaskModel(val userId: String, val taskId: String) : Parcelable