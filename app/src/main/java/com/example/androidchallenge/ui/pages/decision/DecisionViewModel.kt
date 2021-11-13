package com.example.androidchallenge.ui.pages.decision

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidchallenge.core.ResultType
import com.example.androidchallenge.data.repository.interfaces.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DecisionViewModel @Inject constructor(
    private val userRepository: UserRepository
) :
    ViewModel() {


    private val eventChannel = Channel<DecisionEvent>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    init {
        validateUser()
    }

    private fun validateUser() {
        val uri: Uri = Uri.EMPTY
        viewModelScope.launch {
            when (val result =
                withContext(Dispatchers.IO) {
                    userRepository.getCurrentUser()
                }) {
                is ResultType.Error -> {
                    eventChannel.send(DecisionEvent.NavigateLogin)
                }
                is ResultType.Success -> {
                    eventChannel.send(DecisionEvent.NavigateHome(result.data.userId))
                }
            }
        }
    }
}