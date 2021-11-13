package com.example.androidchallenge.data.repository

import android.net.Uri
import com.example.androidchallenge.core.ResultType
import com.example.androidchallenge.data.AppConfig.TASK_COLLECTION
import com.example.androidchallenge.data.AppConfig.USER_COLLECTION
import com.example.androidchallenge.data.failure.CopyTaskFailure
import com.example.androidchallenge.data.model.DeepLinkTaskModel
import com.example.androidchallenge.data.model.ErrorDataModel
import com.example.androidchallenge.data.model.TaskModel
import com.example.androidchallenge.data.repository.interfaces.TaskRepository
import com.example.androidchallenge.ui.pages.task.TaskImage
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
class TaskRepositoryImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage,
) : TaskRepository {

    override fun getTaskByUser(userId: String): Flow<ResultType<List<TaskModel>, ErrorDataModel>> =
        callbackFlow {
            val subscription =
                firebaseFirestore.collection(USER_COLLECTION).document(userId)
                    .collection(TASK_COLLECTION)
                    .addSnapshotListener { snapshot, exception ->
                        exception?.let {
                            val error = ResultType.Error<List<TaskModel>, ErrorDataModel>(
                                ErrorDataModel(
                                    it.code.value(),
                                    it.message
                                )
                            )
                            trySend(error)
                            cancel(it.message.toString())
                        }
                        snapshot?.let {
                            val tasks = it.map { document ->
                                document.toObject(TaskModel::class.java)
                                    .apply { id = document.id }
                            }
                            val successTasks =
                                ResultType.Success<List<TaskModel>, ErrorDataModel>(tasks)
                            trySend(successTasks)
                        }
                    }
            awaitClose { subscription.remove() }
        }

    override suspend fun addTaskUser(
        userId: String,
        task: TaskModel,
        images: List<Uri>
    ): ResultType<String, Exception> {
        try {
            val imagesPath = mutableListOf<String>()
            for (uri in images) {
                val imageUploadResult = addImage(uri = uri)
                if (imageUploadResult is ResultType.Success) {
                    imagesPath.add(imageUploadResult.data)
                }
            }
            val newTask = task.copy(images = imagesPath)
            val docRef =
                firebaseFirestore.collection(USER_COLLECTION).document(userId)
                    .collection(TASK_COLLECTION)
                    .add(newTask).await()
            return ResultType.Success(docRef.id)
        } catch (e: Exception) {
            return ResultType.Error(e)
        }
    }

    override suspend fun updateTaskUser(
        userId: String,
        taskId: String,
        name: String,
        description: String,
        newImages: List<Uri>,
        removedImages: List<String>,
    ): ResultType<Unit, Exception> {
        try {
            val newImagesPath = mutableListOf<String>()
            val removedImagesPath = mutableListOf<String>()

            for (uri in newImages) {
                val imageUploadResult = addImage(uri = uri)
                if (imageUploadResult is ResultType.Success) {
                    newImagesPath.add(imageUploadResult.data)
                }
            }
            for (url in removedImages) {
                val imageDeleteResult = removeImage(url)
                if (imageDeleteResult is ResultType.Success) {
                    removedImagesPath.add(url)
                }
            }
            val taskReference =
                firebaseFirestore.collection(USER_COLLECTION).document(userId).collection(
                    TASK_COLLECTION
                ).document(taskId)
            firebaseFirestore.runTransaction { transaction ->
                transaction.update(
                    taskReference,
                    "name",
                    name
                )
                transaction.update(
                    taskReference,
                    "description",
                    description
                )
                transaction.update(
                    taskReference,
                    "images",
                    FieldValue.arrayUnion(*newImagesPath.toTypedArray())
                )
                transaction.update(
                    taskReference,
                    "images",
                    FieldValue.arrayRemove(*removedImagesPath.toTypedArray())
                )
            }.await()
            return ResultType.Success(Unit)
        } catch (e: Exception) {
            return ResultType.Error(e)
        }
    }

    override suspend fun deleteTaskUser(
        userId: String,
        taskId: String
    ): ResultType<Unit, Exception> {
        return try {
            val taskRef =
                firebaseFirestore.collection(USER_COLLECTION).document(userId)
                    .collection(TASK_COLLECTION)
                    .document(taskId)
            val taskSnapshot = taskRef.get().await()
            val task = taskSnapshot.toObject(TaskModel::class.java)
            task?.images?.forEach { url ->
                when (removeImage(url)) {
                    is ResultType.Error -> {
                    }
                    is ResultType.Success -> {
                    }
                }
            }
            taskRef.delete().await()
            ResultType.Success(Unit)
        } catch (e: Exception) {
            ResultType.Error(e)
        }
    }

    override suspend fun copyTask(
        deepLinkTask: DeepLinkTaskModel,
        userId: String
    ): ResultType<String, CopyTaskFailure> {
        try {
            val taskRef =
                firebaseFirestore.collection(USER_COLLECTION).document(deepLinkTask.userId)
                    .collection(TASK_COLLECTION).document(deepLinkTask.taskId)
            val taskSnapshot = taskRef.get().await()
            val task = taskSnapshot.toObject(TaskModel::class.java)
                ?: return ResultType.Error(CopyTaskFailure.TaskNotFound)
            val newTask = task.copy(images = emptyList())
            val newTaskRef =
                firebaseFirestore.collection(USER_COLLECTION).document(userId).collection(
                    TASK_COLLECTION
                ).add(newTask).await()
            return ResultType.Success(newTaskRef.id)
        } catch (e: Exception) {
            return ResultType.Error(CopyTaskFailure.ServerFailure)
        }
    }

    override suspend fun getImageUrls(imagesPath: List<String>): ResultType<List<TaskImage.TaskImageNetwork>, Exception> {
        val imageUrls = mutableListOf<TaskImage.TaskImageNetwork>()
        for (imagePath in imagesPath) {
            val url = firebaseStorage.reference.child(imagePath).downloadUrl.await()
            imageUrls.add(TaskImage.TaskImageNetwork(url.toString(), imagePath))
        }
        return ResultType.Success(imageUrls)
    }

    private suspend fun addImage(uri: Uri): ResultType<String, Exception> {
        val ref = firebaseStorage.reference.child("images/${UUID.randomUUID()}")
        return try {
            ref.putFile(uri).await()
            ResultType.Success(ref.path)
        } catch (e: Exception) {
            ResultType.Error(e)
        }
    }

    private suspend fun removeImage(path: String): ResultType<Unit, Exception> {
        val ref = firebaseStorage.reference.child(path)
        return try {
            ref.delete().await()
            ResultType.Success(Unit)
        } catch (e: Exception) {
            ResultType.Error(e)
        }
    }

}