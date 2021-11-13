package com.example.androidchallenge.ui.pages.home

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
import com.example.androidchallenge.data.repository.interfaces.UserRepository
import com.example.androidchallenge.ui.util.Constants.TASK_ID
import com.example.androidchallenge.ui.util.Constants.USER_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
    private val taskRepository: TaskRepository,
) : ViewModel() {

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _tasks = mutableStateListOf<TaskModel>()
    val tasks: SnapshotStateList<TaskModel> = _tasks

    private val eventChannel = Channel<HomeEvent>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    init {
        getTasks()
        addPendingTask()
    }

    private fun addPendingTask() {
        savedStateHandle.get<String>(TASK_ID)?.let {

        }
    }

    fun deleteTask(taskId: String) {
        savedStateHandle.get<String>(USER_ID)?.let { userId ->
            viewModelScope.launch {
                when (withContext(Dispatchers.IO) {
                    taskRepository.deleteTaskUser(
                        userId,
                        taskId
                    )
                }) {
                    is ResultType.Error -> {
                        _isLoading.value = false
                        eventChannel.send(HomeEvent.ShowDeleteTaskError)
                    }
                    is ResultType.Success -> {
                        _isLoading.value = false
                        eventChannel.send(HomeEvent.ShowDeleteTaskSuccess)
                    }
                }
            }
        }
    }

    private fun getTasks() {
        savedStateHandle.get<String>(USER_ID)?.let { userId ->
            viewModelScope.launch {
                withContext(Dispatchers.IO) { taskRepository.getTaskByUser(userId) }.collect { result ->
                    when (result) {
                        is ResultType.Error -> {
                        }
                        is ResultType.Success -> {
                            _tasks.clear()
                            _tasks.addAll(result.data)
                        }
                    }
                }
            }
        }
    }

    fun logout() {
        userRepository.logout()
    }

}