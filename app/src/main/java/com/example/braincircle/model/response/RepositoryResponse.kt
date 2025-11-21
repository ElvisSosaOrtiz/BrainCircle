package com.example.braincircle.model.response

interface RepositoryResponse {
    data object Success: RepositoryResponse
    data class Error(val message: String): RepositoryResponse
}