package com.example.braincircle.viewmodel.auth_screens

import com.google.firebase.auth.FirebaseUser

sealed interface AuthUiState {
    object Idle: AuthUiState
    object Loading: AuthUiState
    data class Success(val user: FirebaseUser): AuthUiState
    data class Error(val message: String): AuthUiState
    data class Message(val message: String): AuthUiState
}

