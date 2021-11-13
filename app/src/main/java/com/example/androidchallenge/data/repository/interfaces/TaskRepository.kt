package com.example.androidchallenge.data.repository.interfaces

import android.net.Uri
import com.example.androidchallenge.core.ResultType
import com.example.androidchallenge.data.failure.CopyTaskFailure
import com.example.androidchallenge.data.model.DeepLinkTaskModel
import com.example.androidchallenge.data.model.ErrorDataModel
import com.example.androidchallenge.data.model.TaskModel
import com.example.androidchallenge.ui.pages.task.TaskImage
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getTaskByUser(userId: String): Flow<ResultType<List<TaskModel>, ErrorDataModel>>
    suspend fun addTaskUser(
        userId: String,
        task: TaskModel,
        images: List<Uri>
    ): ResultType<String, Exception>

    suspend fun updateTaskUser(
        userId: String,
        taskId: String,
        name: String,
        description: String,
        newImages: List<Uri>,
        removedImages: List<String>,
    ): ResultType<Unit, Exception>

    suspend fun deleteTaskUser(userId: String, taskId: String): ResultType<Unit, Exception>

    suspend fun copyTask(
        deepLinkTask: DeepLinkTaskModel,
        userId: String
    ): ResultType<String, CopyTaskFailure>

    suspend fun getImageUrls(imagesPath: List<String>): ResultType<List<TaskImage.TaskImageNetwork>, Exception>


}