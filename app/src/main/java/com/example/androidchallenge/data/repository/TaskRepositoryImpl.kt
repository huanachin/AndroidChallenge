package com.example.androidchallenge.data.repository

import com.example.androidchallenge.core.ResultType
import com.example.androidchallenge.data.AppConfig.TASK_COLLECTION
import com.example.androidchallenge.data.AppConfig.USER_COLLECTION
import com.example.androidchallenge.data.model.ErrorDataModel
import com.example.androidchallenge.data.model.TaskModel
import com.example.androidchallenge.data.repository.interfaces.TaskRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

@ExperimentalCoroutinesApi
class TaskRepositoryImpl(private val firestore: FirebaseFirestore) : TaskRepository {

    override fun getTaskByUser(uuid: String): Flow<ResultType<List<TaskModel>, ErrorDataModel>> =
        callbackFlow {
            val subscription =
                firestore.collection(USER_COLLECTION).document(uuid).collection(TASK_COLLECTION)
                    .addSnapshotListener { snapshot, exception ->
                        exception?.let {
                            trySend(
                                ResultType.Error(
                                    ErrorDataModel(
                                        it.code.value(),
                                        it.message
                                    )
                                )
                            )
                            cancel(it.message.toString())
                        }
                        snapshot?.let {
                            val tasks = it.map { document ->
                                document.toObject(TaskModel::class.java)
                                    .apply { id = document.id }
                            }
                            trySend(ResultType.Success<List<TaskModel>, ErrorDataModel>(tasks))
                        }
                    }
            awaitClose { subscription.remove() }
        }

    override suspend fun addTaskUser(
        uuid: String,
        task: TaskModel
    ): ResultType<String, Exception> {
        return try {
            val docRef =
                firestore.collection(USER_COLLECTION).document(uuid).collection(TASK_COLLECTION)
                    .add(task).await()
            ResultType.Success(docRef.id)
        } catch (e: Exception) {
            ResultType.Error(e)
        }
    }

    override suspend fun updateTaskUser(
        uuid: String,
        taskId: String,
        task: TaskModel
    ): ResultType<Unit, Exception> {
        return try {
            firestore.collection(USER_COLLECTION).document(uuid).collection(TASK_COLLECTION)
                .document(taskId).set(task).await()
            ResultType.Success(Unit)
        } catch (e: Exception) {
            ResultType.Error(e)
        }
    }

    override suspend fun deleteTaskUser(
        uuid: String,
        taskId: String
    ): ResultType<Unit, Exception> {
        return try {
            firestore.collection(USER_COLLECTION).document(uuid).collection(TASK_COLLECTION)
                .document(taskId).delete().await()
            ResultType.Success(Unit)
        } catch (e: Exception) {
            ResultType.Error(e)
        }
    }
}