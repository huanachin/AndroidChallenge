package com.example.androidchallenge.di

import com.example.androidchallenge.data.repository.TaskRepositoryImpl
import com.example.androidchallenge.data.repository.UserRepositoryImpl
import com.example.androidchallenge.data.repository.interfaces.TaskRepository
import com.example.androidchallenge.data.repository.interfaces.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun provideUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

    @Binds
    fun provideTaskRepository(taskRepositoryImpl: TaskRepositoryImpl): TaskRepository

}