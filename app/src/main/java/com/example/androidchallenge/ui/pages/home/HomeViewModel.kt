package com.example.androidchallenge.ui.pages.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidchallenge.core.ResultType
import com.example.androidchallenge.data.model.TaskModel
import com.example.androidchallenge.data.repository.TaskRepositoryImpl
import com.example.androidchallenge.data.repository.UserRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@ExperimentalCoroutinesApi
class HomeViewModel : ViewModel() {

    private val userRepository =
        UserRepositoryImpl(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())
    private val taskRepository = TaskRepositoryImpl(FirebaseFirestore.getInstance())

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _tasks = mutableStateListOf<TaskModel>()
    val tasks: SnapshotStateList<TaskModel> = _tasks

    private val eventChannel = Channel<HomeEvent>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    init {
        getTasks()
    }

    fun logout() {
        userRepository.logout()
    }

    fun deleteTask(taskId: String) {
        viewModelScope.launch {
            _isLoading.value = false
            when (val userResult =
                withContext(Dispatchers.IO) { (userRepository.getCurrentUser()) }) {
                is ResultType.Error -> {
                    _isLoading.value = false
                    eventChannel.send(HomeEvent.ShowDeleteTaskError)
                }
                is ResultType.Success -> {
                    userResult.data.uuid?.let { uuid ->
                        when (withContext(Dispatchers.IO) {
                            taskRepository.deleteTaskUser(uuid, taskId)
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
        }
    }

    private fun getTasks() {
        viewModelScope.launch {
            when (val userResult =
                withContext(Dispatchers.IO) { (userRepository.getCurrentUser()) }) {
                is ResultType.Error -> {
                }
                is ResultType.Success -> {
                    withContext(Dispatchers.IO) {
                        userResult.data.uuid?.let { uuid ->
                            taskRepository.getTaskByUser(uuid).collect {
                                when (it) {
                                    is ResultType.Error -> {
                                    }
                                    is ResultType.Success -> {
                                        _tasks.clear()
                                        _tasks.addAll(it.data)
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }
    }
}