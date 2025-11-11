package com.example.braincircle.model

interface AuthResponse {
    data object Success: AuthResponse
    data class Error(val message: String): AuthResponse
}