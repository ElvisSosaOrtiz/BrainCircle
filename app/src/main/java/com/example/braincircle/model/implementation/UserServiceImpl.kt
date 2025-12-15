package com.example.braincircle.model.implementation

import android.net.Uri
import com.example.braincircle.model.response.RepositoryResponse
import com.example.braincircle.model.service.AuthRepository
import com.example.braincircle.model.service.FirestoreRepository
import com.example.braincircle.model.service.UserService
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

class UserServiceImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val firestoreRepository: FirestoreRepository
) : UserService {
    override suspend fun createUserFromAuth(
        email: String,
        password: String,
        username: String,
        photoUri: Uri?
    ): Flow<RepositoryResponse> = callbackFlow {
        authRepository.signUpWithEmail(email, password)
            .catch { e ->
                trySend(
                    RepositoryResponse.Error(
                        message = e.localizedMessage ?: "Unknown error signing up"
                    )
                )
            }
            .collect { response ->
                if (response is RepositoryResponse.Error) {
                    trySend(response)
                } else {
                    val user = authRepository.currentUser()

                    if (user != null) {
                        authRepository.updateUserProfile(username, photoUri)
                            .catch { e ->
                                trySend(
                                    RepositoryResponse.Error(
                                        message = e.localizedMessage ?: "Unknown error updating user profile"
                                    )
                                )
                            }
                            .collect { userProfile ->
                                authRepository.reloadUser()
                                firestoreRepository.createUserProfile(userProfile ?: return@collect)
                                    .catch { e ->
                                        trySend(
                                            RepositoryResponse.Error(
                                                message = e.localizedMessage ?: "Unknown error creating user profile"
                                            )
                                        )
                                    }
                                    .collect { response ->
                                        if (response is RepositoryResponse.Error) {
                                            trySend(response)
                                        } else {
                                            trySend(RepositoryResponse.Success)
                                        }
                                    }
                            }
                    }
                }
            }
        awaitClose()
    }
}