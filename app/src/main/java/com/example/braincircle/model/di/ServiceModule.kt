package com.example.braincircle.model.di

import com.example.braincircle.model.implementation.UserServiceImpl
import com.example.braincircle.model.service.AuthRepository
import com.example.braincircle.model.service.FirestoreRepository
import com.example.braincircle.model.service.UserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    @Provides
    fun provideUserService(
        authRepository: AuthRepository,
        firestoreRepository: FirestoreRepository
    ): UserService = UserServiceImpl(authRepository, firestoreRepository)
}