package com.example.androidchallenge.data.repository

import com.example.androidchallenge.core.ResultType
import com.example.androidchallenge.data.repository.interfaces.UserRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import io.mockk.*
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runners.JUnit4

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
    fun authenticate() {
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
            assertTrue(result is ResultType.Success)
            if (result is ResultType.Success) {
                assertEquals(result.data.userId, userId)
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