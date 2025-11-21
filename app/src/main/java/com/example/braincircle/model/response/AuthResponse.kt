package com.example.braincircle.model.response

interface AuthResponse {
    data object Success: AuthResponse
    data class Error(val message: String): AuthResponse
}