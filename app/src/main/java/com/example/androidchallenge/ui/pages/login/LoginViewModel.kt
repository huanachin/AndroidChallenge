package com.example.androidchallenge.ui.pages.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidchallenge.core.ResultType
import com.example.androidchallenge.data.failure.LoginFailure
import com.example.androidchallenge.data.repository.interfaces.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _hasError = mutableStateOf(false)
    val hasError: State<Boolean> = _hasError

    private val _isEnabled = mutableStateOf(false)
    val isEnabled: State<Boolean> = _isEnabled

    private val _username = mutableStateOf("")
    val username: State<String> = _username

    private val _password = mutableStateOf("")
    val password: State<String> = _password

    private val eventChannel = Channel<LoginEvent>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    fun authenticate() {
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = withContext(Dispatchers.IO) {
                userRepository.authenticate(
                    _username.value,
                    _password.value
                )
            }) {
                is ResultType.Error -> {
                    _isLoading.value = false
                    when (result.error) {
                        LoginFailure.ServerFailure -> {
                            eventChannel.send(LoginEvent.ShowError(result.error))
                        }
                        LoginFailure.WrongEmailPasswordFailure -> {
                            _hasError.value = true
                        }
                    }
                }
                is ResultType.Success -> {
                    _isLoading.value = false
                    eventChannel.send(LoginEvent.NavigateHome(result.data.userId))
                }
            }
        }
    }

    private fun validateIsEnabled() {
        _isEnabled.value = _username.value.isNotEmpty() && _password.value.isNotEmpty()
    }

    fun onUsernameChanged(username: String) {
        _username.value = username
        _hasError.value = false
        validateIsEnabled()
    }

    fun onPasswordChanged(password: String) {
        _password.value = password
        _hasError.value = false
        validateIsEnabled()
    }

}