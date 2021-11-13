package com.example.androidchallenge.ui.pages.task

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidchallenge.core.ResultType
import com.example.androidchallenge.data.model.TaskModel
import com.example.androidchallenge.data.repository.interfaces.TaskRepository
import com.example.androidchallenge.ui.util.Constants.TASK
import com.example.androidchallenge.ui.util.Constants.USER_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class TaskViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _name = mutableStateOf("")
    val name: State<String> = _name

    private val _description = mutableStateOf("")
    val description: State<String> = _description

    private val _isEnabled = mutableStateOf(false)
    val isEnabled: State<Boolean> = _isEnabled

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _images = mutableStateListOf<TaskImage>()
    val images: SnapshotStateList<TaskImage> = _images

    private val eventChannel = Channel<TaskEvent>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    private var task: TaskModel? = null

    init {
        savedStateHandle.get<TaskModel>(TASK)?.let { task ->
            this.task = task
            _name.value = task.name.orEmpty()
            _description.value = task.description.orEmpty()
            _isEnabled.value = true
            viewModelScope.launch {
                when (val imagesResult = taskRepository.getImageUrls(task.images)) {
                    is ResultType.Error -> {

                    }
                    is ResultType.Success -> {
                        _images.addAll(imagesResult.data)
                    }
                }
            }
        }
    }

    fun onSave() {
        task?.let {
            updateTask(it)
        } ?: addTask()
    }

    private fun addTask() {
        savedStateHandle.get<String>(USER_ID)?.let { userId ->
            viewModelScope.launch {
                _isLoading.value = true
                val name = _name.value
                val description = _description.value
                val images =
                    _images.toList().filterIsInstance<TaskImage.TaskImageLocal>().map { it.uri }
                val taskResult =
                    withContext(Dispatchers.IO) {
                        taskRepository.addTaskUser(
                            userId = userId,
                            task = TaskModel(name = name, description = description),
                            images = images
                        )
                    }
                when (taskResult) {
                    is ResultType.Error -> {
                        eventChannel.send(TaskEvent.ShowError)
                        _isLoading.value = false

                    }
                    is ResultType.Success -> {
                        eventChannel.send(TaskEvent.NavigateBack)
                        _isLoading.value = true
                    }
                }
            }
        }
    }

    private fun updateTask(
        task: TaskModel
    ) {
        savedStateHandle.get<String>(USER_ID)?.let { userId ->
            viewModelScope.launch {
                _isLoading.value = true
                val taskId = task.id
                if (taskId != null) {
                    val newImages =
                        _images.toList().filterIsInstance<TaskImage.TaskImageLocal>().map { it.uri }
                    val networkImages =
                        _images.toList().filterIsInstance<TaskImage.TaskImageNetwork>()
                            .map { it.path }
                    val removedImages = task.images.toSet().subtract(networkImages).toList()
                    val result = withContext(Dispatchers.IO) {
                        taskRepository.updateTaskUser(
                            userId = userId,
                            taskId = taskId,
                            name = _name.value,
                            description = _description.value,
                            newImages = newImages,
                            removedImages = removedImages
                        )
                    }
                    when (result) {
                        is ResultType.Error -> {
                            _isLoading.value = false
                            eventChannel.send(TaskEvent.ShowError)
                        }
                        is ResultType.Success -> {
                            _isLoading.value = false
                            eventChannel.send(TaskEvent.NavigateBack)
                        }
                    }
                }
            }
        }
    }

    private fun validate() {
        _isEnabled.value = _name.value.isNotEmpty() && _description.value.isNotEmpty()
    }

    fun removeImage(index: Int) {
        _images.removeAt(index)
    }

    fun addImage(image: TaskImage) {
        _images.add(image)
    }

    fun onNameChanged(name: String) {
        _name.value = name
        validate()
    }

    fun onDescriptionChanged(description: String) {
        _description.value = description
        validate()
    }

}