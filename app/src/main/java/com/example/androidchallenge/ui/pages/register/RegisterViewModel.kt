package com.example.androidchallenge.ui.pages.register

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidchallenge.core.ResultType
import com.example.androidchallenge.data.failure.RegisterFailure
import com.example.androidchallenge.data.repository.UserRepositoryImpl
import com.example.androidchallenge.ui.util.Validators.isEmailValid
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterViewModel : ViewModel() {

    private val userRepository =
        UserRepositoryImpl(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _username = mutableStateOf("")
    val username: State<String> = _username

    private val _password = mutableStateOf("")
    val password: State<String> = _password

    private val _confirmedPassword = mutableStateOf("")
    val confirmedPassword: State<String> = _confirmedPassword

    private val _isUsernameValid = mutableStateOf(false)
    val isUsernameValid: State<Boolean> = _isUsernameValid

    private val _isConfirmedPasswordValid = mutableStateOf(true)
    val isConfirmedPasswordValid: State<Boolean> = _isConfirmedPasswordValid

    private val _isEnabled = mutableStateOf(false)
    val isEnabled: State<Boolean> = _isEnabled

    private val _hasError = mutableStateOf(false)
    val hasError: State<Boolean> = _hasError

    private val eventChannel = Channel<RegisterEvent>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    fun register() {
        viewModelScope.launch {
            _isLoading.value = true
            when (val result =
                withContext(Dispatchers.IO) {
                    userRepository.register(
                        _username.value,
                        _password.value
                    )
                }) {
                is ResultType.Error -> {
                    _isLoading.value = false
                    when (result.error) {
                        RegisterFailure.ServerFailure -> {
                            eventChannel.send(RegisterEvent.ShowError(result.error))
                        }
                        RegisterFailure.WrongEmailPasswordFailure -> {
                            _hasError.value = true
                        }
                    }
                }
                is ResultType.Success -> {
                    _isLoading.value = false
                    eventChannel.send(RegisterEvent.NavigateHome)
                }
            }
        }
    }

    private fun validateIsEnabled() {
        _isEnabled.value =
            _isUsernameValid.value && _password.value.isNotEmpty() && _isConfirmedPasswordValid.value
    }

    private fun validatePassword() {
        val confirmedPassword = _confirmedPassword.value
        val password = _password.value
        _isConfirmedPasswordValid.value = confirmedPassword == password
    }

    fun onUsernameChanged(username: String) {
        _username.value = username
        _isUsernameValid.value = isEmailValid(username)
        _hasError.value = false
        validateIsEnabled()
    }

    fun onPasswordChanged(password: String) {
        _password.value = password
        _hasError.value = false
        validatePassword()
        validateIsEnabled()

    }

    fun onConfirmedPasswordChanged(password: String) {
        _confirmedPassword.value = password
        _hasError.value = false
        validatePassword()
        validateIsEnabled()
    }
}