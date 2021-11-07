package com.example.androidchallenge.ui.pages.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidchallenge.core.ResultType
import com.example.androidchallenge.data.repository.UserRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashViewModel : ViewModel() {

    private val userRepository =
        UserRepositoryImpl(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())

    private val eventChannel = Channel<SplashEvent>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    init {
        validateUser()
    }

    private fun validateUser() {
        viewModelScope.launch {
            when (val result =
                withContext(Dispatchers.IO) {
                    userRepository.getCurrentUser()
                }) {
                is ResultType.Error -> {
                    eventChannel.send(SplashEvent.NavigateLogin)
                }
                is ResultType.Success -> {
                    eventChannel.send(SplashEvent.NavigateHome)
                }
            }
        }
    }
}