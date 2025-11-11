package com.example.braincircle.model.service

import android.content.Context
import com.example.braincircle.model.AuthResponse
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun signInWithEmail(email: String, password: String): Flow<AuthResponse>
    suspend fun signUpWithEmail(email: String, password: String): Flow<AuthResponse>
    suspend fun signInWithGoogle(@ApplicationContext context: Context): Flow<AuthResponse>
    suspend fun sendPasswordReset(email: String): Flow<AuthResponse>
    fun signOut()
    fun currentUser(): FirebaseUser?
    fun isUserSignedIn(): Boolean
}