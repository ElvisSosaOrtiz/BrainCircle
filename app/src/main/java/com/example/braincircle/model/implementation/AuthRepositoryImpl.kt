package com.example.braincircle.model.implementation

import android.content.Context
import android.net.Uri
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.example.braincircle.R
import com.example.braincircle.model.AuthResponse
import com.example.braincircle.model.service.AuthRepository
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.security.MessageDigest
import java.util.UUID
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRepository {

    override suspend fun signInWithEmail(
        email: String,
        password: String
    ): Flow<AuthResponse> = callbackFlow {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    trySend(AuthResponse.Success)
                } else {
                    trySend(
                        AuthResponse.Error(
                            message = task.exception?.localizedMessage ?: "Unknown error while signing in"
                        )
                    )
                }
            }
        awaitClose()
    }

    override suspend fun signUpWithEmail(
        email: String,
        password: String,
        username: String,
        photoUri: Uri?
    ): Flow<AuthResponse> = callbackFlow {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val profileUpdate = UserProfileChangeRequest.Builder()
                        .setDisplayName(username)
                        .setPhotoUri(photoUri)
                        .build()
                    auth.currentUser?.updateProfile(profileUpdate)
                        ?.addOnCompleteListener { updateUserTask ->
                            if (updateUserTask.isSuccessful) {
                                trySend(AuthResponse.Success)
                            } else {
                                trySend(
                                    AuthResponse.Error(
                                        message = updateUserTask.exception?.localizedMessage
                                            ?: "Unknown error while updating user profile"
                                    )
                                )
                            }
                        }
                } else {
                    trySend(
                        AuthResponse.Error(
                            message = task.exception?.localizedMessage
                                ?: "Unknown error while creating account"
                        )
                    )
                }
            }
        awaitClose()
    }

    override suspend fun signInWithGoogle(@ApplicationContext context: Context): Flow<AuthResponse> =
        callbackFlow {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(context.getString(R.string.default_web_client_id))
                .setAutoSelectEnabled(false)
                .setNonce(createNonce())
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            try {
                val credentialManager = CredentialManager.create(context)
                val result = credentialManager.getCredential(
                    context = context,
                    request = request
                )

                val credential = result.credential
                if (credential is CustomCredential) {
                    if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                        try {
                            val googleIdTokenCredential = GoogleIdTokenCredential
                                .createFrom(credential.data)

                            val firebaseCredential = GoogleAuthProvider
                                .getCredential(
                                    googleIdTokenCredential.idToken,
                                    null
                                )

                            auth.signInWithCredential(firebaseCredential)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        trySend(AuthResponse.Success)
                                    } else {
                                        trySend(
                                            AuthResponse.Error(
                                                message = task.exception?.localizedMessage
                                                    ?: "Unknown error while signing in with Google"
                                            )
                                        )
                                    }
                                }
                        } catch (e: GoogleIdTokenParsingException) {
                            trySend(
                                AuthResponse.Error(
                                    message = e.localizedMessage
                                        ?: "Unknown error while parsing Google ID token"
                                )
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                trySend(
                    AuthResponse.Error(
                        message = e.localizedMessage
                            ?: "Unknown error while creating custom credential"
                    )
                )
            }
            awaitClose()
        }

    override suspend fun sendPasswordReset(email: String): Flow<AuthResponse> = callbackFlow {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    trySend(AuthResponse.Success)
                } else {
                    trySend(
                        AuthResponse.Error(
                            message = task.exception?.localizedMessage
                                ?: "Unknown error while sending password reset"
                        )
                    )
                }
            }
        awaitClose()
    }

    override suspend fun getAuthStateFlow(): Flow<FirebaseUser?> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser).isSuccess
        }
        auth.addAuthStateListener(authStateListener)
        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }

    override fun isUserSignedIn(): Boolean {
        val instance = FirebaseAuth.getInstance()
        instance.apply {
            currentUser?.reload()
            return currentUser != null
        }
    }

    override fun signOut() = auth.signOut()

    override fun currentUser(): FirebaseUser? = auth.currentUser

    private fun createNonce(): String {
        val rawNonce = UUID.randomUUID().toString()
        val bytes = rawNonce.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)

        return digest.fold("") { str, it ->
            str + "%02x".format(it)
        }
    }
}