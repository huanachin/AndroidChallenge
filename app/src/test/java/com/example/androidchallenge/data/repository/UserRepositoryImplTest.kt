package com.example.androidchallenge.data.repository

import com.example.androidchallenge.core.ResultType
import com.example.androidchallenge.data.failure.LoginFailure
import com.example.androidchallenge.data.repository.interfaces.UserRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.firestore.FirebaseFirestore
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.lang.Exception

class UserRepositoryImplTest {


    @MockK
    private lateinit var firebaseAuth: FirebaseAuth

    @MockK
    private lateinit var firebaseFirestore: FirebaseFirestore

    private lateinit var userRepository: UserRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        userRepository = UserRepositoryImpl(firebaseAuth, firebaseFirestore)
    }

    @Test
    fun `Authentication success`() {
        val username = "abc@gmail.com"
        val password = "12345678"
        val userId = "Azk9fF1DF4J"

        val mockResult = mockk<AuthResult>()
        every { mockResult.user } returns mockk {
            every { uid } returns userId
        }
        val taskResult = mockk<Task<AuthResult>> {
            every { result } returns mockResult
            every { exception } returns null
            every { isCanceled } returns false
            every { isComplete } returns true
        }

        every { firebaseAuth.signInWithEmailAndPassword(username, password) } returns taskResult

        runBlocking {
            val result = userRepository.authenticate(username, password)
            assert(result is ResultType.Success)
            if (result is ResultType.Success) {
                assertEquals(result.data.userId, userId)
            }

        }
    }

    @Test
    fun `Authentication error wrong email and password`() {
        val username = "abc@gmail.com"
        val password = "12345678"

        val taskResult = mockk<Task<AuthResult>> {
            every { result } returns mockk()
            every { exception } returns FirebaseAuthInvalidCredentialsException("1", "1")
            every { isCanceled } returns false
            every { isComplete } returns true
        }

        every { firebaseAuth.signInWithEmailAndPassword(username, password) } returns taskResult

        runBlocking {
            val result = userRepository.authenticate(username, password)
            assertTrue(result is ResultType.Error)
            if (result is ResultType.Error) {
                assertEquals(result.error, LoginFailure.WrongEmailPasswordFailure)
            }
        }
    }

    @Test
    fun `Authentication error server error`() {
        val username = "abc@gmail.com"
        val password = "12345678"

        val taskResult = mockk<Task<AuthResult>> {
            every { result } returns mockk()
            every { exception } returns Exception()
            every { isCanceled } returns false
            every { isComplete } returns true
        }

        every { firebaseAuth.signInWithEmailAndPassword(username, password) } returns taskResult

        runBlocking {
            val result = userRepository.authenticate(username, password)
            assertTrue(result is ResultType.Error)
            if (result is ResultType.Error) {
                assertEquals(result.error, LoginFailure.ServerFailure)
            }
        }
    }


    @Test
    fun register() {

    }

    @Test
    fun getCurrentUser() {
    }

    @Test
    fun logout() {
    }
}