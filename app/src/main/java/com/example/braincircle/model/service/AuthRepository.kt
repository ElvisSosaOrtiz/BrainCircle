package com.example.braincircle.model.service

import android.content.Context
import android.net.Uri
import com.example.braincircle.model.data.User
import com.example.braincircle.model.response.RepositoryResponse
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun signInWithEmail(email: String, password: String): Flow<RepositoryResponse>
    suspend fun signUpWithEmail(
        email: String,
        password: String,
        username: String,
        photoUri: Uri?
    ): Flow<FirebaseUser?>

    suspend fun signInWithGoogle(@ApplicationContext context: Context): Flow<RepositoryResponse>
    suspend fun sendPasswordReset(email: String): Flow<RepositoryResponse>
    fun signOut()
    fun currentUser(): FirebaseUser?
    suspend fun getAuthStateFlow(): Flow<FirebaseUser?>
    suspend fun reloadUser(): FirebaseUser?
    suspend fun updateUserProfile(username: String, photoUri: Uri?): Flow<User?>
}