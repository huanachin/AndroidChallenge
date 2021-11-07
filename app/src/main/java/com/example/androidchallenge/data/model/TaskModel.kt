package com.example.androidchallenge.data.model

import com.google.firebase.firestore.GeoPoint

data class TaskModel(
    val name: String? = "",
    val description: String? = "",
    val position: GeoPoint? = null,
    val images: List<String> = emptyList()
) : Model()

fun getTasks(): List<TaskModel> {
    return (0..20).map {
        TaskModel(
            name = "TAREA $it",
            description = "Descripcion $it"
        )
    }
}