package com.example.braincircle.model.response

interface DatabaseResponse {
    data object Success: DatabaseResponse
    data class Error(val message: String): DatabaseResponse
}