package com.example.androidchallenge.data.repository

import com.example.androidchallenge.data.repository.interfaces.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class UserRepositoryImplTest {

    @Mock
    private lateinit var firebaseAuth: FirebaseAuth

    @Mock
    private lateinit var firebaseFirestore: FirebaseFirestore

    private lateinit var userRepository: UserRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        userRepository = UserRepositoryImpl(firebaseAuth, firebaseFirestore)
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