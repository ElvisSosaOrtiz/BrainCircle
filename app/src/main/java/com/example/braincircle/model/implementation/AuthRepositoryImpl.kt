package com.example.braincircle.model.implementation

import com.example.braincircle.model.service.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val auth: FirebaseAuth) : AuthRepository {
    override suspend fun signInWithEmail(
        email: String,
        password: String
    ): Result<FirebaseUser?> {
        return suspendCancellableCoroutine { continuation ->
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    continuation.resume(Result.success(auth.currentUser)) { cause, _, _ ->
                        null?.let { it(cause) }
                    }
                } else {
                    continuation.resume(Result.failure(task.exception ?: Exception("Unknown exception"))) { cause, _, _ ->
                        null?.let { it(cause) }
                    }
                }
            }
        }
    }

    override suspend fun signUpWithEmail(
        email: String,
        password: String
    ): Result<FirebaseUser?> {
        return suspendCancellableCoroutine { continuation ->
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    continuation.resume(Result.success(auth.currentUser)) { cause, _, _ ->
                        null?.let { it(cause) }
                    }
                } else {
                    continuation.resume(Result.failure(task.exception ?: Exception("Unknown error"))) { cause, _, _ ->
                        null?.let { it(cause) }
                    }
                }
            }
        }
    }

    override suspend fun signInWithGoogle(idToken: String): Result<FirebaseUser?> {
        return suspendCancellableCoroutine { continuation ->
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            auth.signInWithCredential(credential).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    continuation.resume(Result.success(auth.currentUser)) { cause, _, _ ->
                        null?.let { it(cause) }
                    }
                } else {
                    continuation.resume(Result.failure(task.exception ?: Exception("Unknown error"))) { cause, _, _ ->
                        null?.let { it(cause) }
                    }
                }
            }
        }
    }

    override fun signOut() = auth.signOut()

    override fun currentUser(): FirebaseUser? = auth.currentUser
}