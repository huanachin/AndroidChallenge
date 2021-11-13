package com.example.androidchallenge.ui.pages.splash

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidchallenge.core.ResultType
import com.example.androidchallenge.data.repository.interfaces.UserRepository
import com.example.androidchallenge.ui.util.Constants.TASK_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository
) :
    ViewModel() {

    private val eventChannel = Channel<SplashEvent>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    init {
        val params = savedStateHandle.get<String>(TASK_ID)
        validateUser(params)
    }

    private fun validateUser(params: String?) {
        viewModelScope.launch {
            when (val result =
                withContext(Dispatchers.IO) {
                    userRepository.getCurrentUser()
                }) {
                is ResultType.Error -> {
                    eventChannel.send(SplashEvent.NavigateLogin)
                }
                is ResultType.Success -> {
                    eventChannel.send(SplashEvent.NavigateHome(result.data.userId, params))
                }
            }
        }
    }
}