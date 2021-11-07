package com.example.androidchallenge.data.repository

import com.example.androidchallenge.core.ResultType
import com.example.androidchallenge.data.AppConfig.USER_COLLECTION
import com.example.androidchallenge.data.failure.CurrentUserFailure
import com.example.androidchallenge.data.failure.LoginFailure
import com.example.androidchallenge.data.failure.RegisterFailure
import com.example.androidchallenge.data.model.UserModel
import com.example.androidchallenge.data.repository.interfaces.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) : UserRepository {

    override suspend fun authenticate(
        username: String,
        password: String,
    ): ResultType<UserModel, LoginFailure> {
        try {
            val authResult = firebaseAuth.signInWithEmailAndPassword(username, password).await()
            val user = authResult?.user ?: return ResultType.Error(LoginFailure.ServerFailure)
            return ResultType.Success(UserModel(user.uid))
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            return ResultType.Error(LoginFailure.WrongEmailPasswordFailure)
        } catch (e: Exception) {
            return ResultType.Error(LoginFailure.ServerFailure)
        }
    }

    override suspend fun register(
        username: String,
        password: String
    ): ResultType<UserModel, RegisterFailure> {
        try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(username, password).await()
            val user = authResult?.user ?: return ResultType.Error(RegisterFailure.ServerFailure)
            firebaseFirestore.collection(USER_COLLECTION).document(user.uid).set(UserModel())
                .await()
            return ResultType.Success(UserModel(user.uid))
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            return ResultType.Error(RegisterFailure.WrongEmailPasswordFailure)
        } catch (e: Exception) {
            return ResultType.Error(RegisterFailure.ServerFailure)
        }
    }

    override suspend fun getCurrentUser(): ResultType<UserModel, CurrentUserFailure> {
        try {
            val user =
                firebaseAuth.currentUser ?: return ResultType.Error(CurrentUserFailure.UserNotFound)
            return ResultType.Success(UserModel(user.uid))
        } catch (e: Exception) {
            return ResultType.Error(CurrentUserFailure.ServerFailure)
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }
}