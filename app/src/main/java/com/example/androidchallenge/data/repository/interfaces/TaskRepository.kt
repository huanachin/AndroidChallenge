package com.example.androidchallenge.data.repository.interfaces

import com.example.androidchallenge.core.ResultType
import com.example.androidchallenge.data.model.ErrorDataModel
import com.example.androidchallenge.data.model.TaskModel
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getTaskByUser(uuid: String): Flow<ResultType<List<TaskModel>, ErrorDataModel>>
    suspend fun addTaskUser(uuid: String, task: TaskModel): ResultType<String, Exception>
    suspend fun updateTaskUser(
        uuid: String,
        taskId: String,
        task: TaskModel
    ): ResultType<Unit, Exception>

    suspend fun deleteTaskUser(uuid: String, taskId: String): ResultType<Unit, Exception>
}