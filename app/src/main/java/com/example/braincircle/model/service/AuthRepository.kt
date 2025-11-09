package com.example.braincircle.model.service

import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    suspend fun signInWithEmail(email: String, password: String): Result<FirebaseUser?>
    suspend fun signUpWithEmail(email: String, password: String): Result<FirebaseUser?>
    suspend fun signInWithGoogle(idToken: String): Result<FirebaseUser?>
    suspend fun sendPasswordReset(email: String): Result<Unit?>
    fun signOut()
    fun currentUser(): FirebaseUser?
}