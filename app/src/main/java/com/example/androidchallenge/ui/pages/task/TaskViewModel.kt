package com.example.androidchallenge.ui.pages.task

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@ExperimentalCoroutinesApi
class TaskViewModel(task: TaskModel? = null) : ViewModel() {

    private val taskRepository = TaskRepositoryImpl(FirebaseFirestore.getInstance())
    private val userRepository =
        UserRepositoryImpl(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())

    private val _name = mutableStateOf(task?.name.orEmpty())
    val name: State<String> = _name

    private val _description = mutableStateOf(task?.description.orEmpty())
    val description: State<String> = _description

    private val _isEnabled = mutableStateOf(task != null)
    val isEnabled: State<Boolean> = _isEnabled

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    fun addTask(
        onAddTaskSuccess: () -> Unit,
        onAddTaskError: () -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            when (val userResult =
                withContext(Dispatchers.IO) { userRepository.getCurrentUser() }) {
                is ResultType.Error -> {
                    _isLoading.value = false
                    onAddTaskError()
                }
                is ResultType.Success -> {
                    userResult.data.uuid?.let {
                        val name = _name.value
                        val description = _description.value
                        val taskResult = withContext(Dispatchers.IO) {
                            taskRepository.addTaskUser(
                                it,
                                TaskModel(name = name, description = description)
                            )
                        }
                        when (taskResult) {
                            is ResultType.Error -> {
                                onAddTaskError()
                                _isLoading.value = false
                            }
                            is ResultType.Success -> {
                                onAddTaskSuccess()
                                _isLoading.value = true

                            }
                        }
                    }

                }
            }
        }
    }

    fun updateTask(
        task: TaskModel,
        onEditTaskSuccess: () -> Unit,
        onEditTaskError: () -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = false
            when (val userResult =
                withContext(Dispatchers.IO) { (userRepository.getCurrentUser()) }) {
                is ResultType.Error -> {
                    _isLoading.value = false
                    onEditTaskError()
                }
                is ResultType.Success -> {
                    val userId = userResult.data.uuid
                    val taskId = task.id
                    if (userId != null && taskId != null) {
                        when (withContext(Dispatchers.IO) {
                            taskRepository.updateTaskUser(
                                userId,
                                taskId,
                                task.copy(
                                    name = _name.value,
                                    description = _description.value
                                )
                            )
                        }) {
                            is ResultType.Error -> {
                                _isLoading.value = false
                                onEditTaskSuccess()
                            }
                            is ResultType.Success -> {
                                _isLoading.value = false
                                onEditTaskSuccess()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun validate() {
        _isEnabled.value = _name.value.isNotEmpty() && _description.value.isNotEmpty()
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