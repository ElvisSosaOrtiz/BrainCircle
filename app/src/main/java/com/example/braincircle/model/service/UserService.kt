package com.example.braincircle.model.service

import android.net.Uri
import com.example.braincircle.model.data.User
import com.example.braincircle.model.response.RepositoryResponse
import kotlinx.coroutines.flow.Flow

interface UserService {
    suspend fun createUserFromAuth(
        email: String,
        password: String,
        username: String,
        photoUri: Uri?
    ): Flow<RepositoryResponse>
}