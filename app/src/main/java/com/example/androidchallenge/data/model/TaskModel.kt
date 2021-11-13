package com.example.androidchallenge.data.model

import android.os.Parcelable
import com.google.firebase.firestore.GeoPoint
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.TypeParceler

@Parcelize
@TypeParceler<GeoPoint?, GeoPointParceler>()
data class TaskModel(
    val name: String? = "",
    val description: String? = "",
    val position: GeoPoint? = null,
    val images: List<String> = emptyList()
) : Model(), Parcelable