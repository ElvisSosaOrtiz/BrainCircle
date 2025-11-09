package com.example.braincircle

import android.credentials.GetCredentialException
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.PrepareGetCredentialResponse
import androidx.lifecycle.lifecycleScope
import com.example.braincircle.ui.theme.BrainCircleTheme
import com.example.braincircle.view.BrainCircleApp
import com.example.braincircle.viewmodel.auth_screens.AuthViewModel
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var credentialManager: CredentialManager
    @Inject
    lateinit var getCredentialRequest: GetCredentialRequest

    private val authViewModel: AuthViewModel by viewModels()

    private val credentialExecutor by lazy { Executors.newSingleThreadExecutor() }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        var keepOn = true
        splashScreen.setKeepOnScreenCondition { keepOn }
        setContent {
            BrainCircleTheme {
                BrainCircleApp(authViewModel = authViewModel)
            }
        }
        lifecycleScope.launch {
            delay(700)
            keepOn = false
        }
    }

    private fun beginCredentialManagerSignIn() {
        lifecycleScope.launch {
            try {
                val prepareResponse: PrepareGetCredentialResponse = try {
                    withContext(Dispatchers.IO) {
                        credentialManager.prepareGetCredential(getCredentialRequest)
                    }
                } catch (e: GetCredentialException) {
                    runOnUiThread {
                        authViewModel.setError(e.localizedMessage ?: "No credentials available")
                    }
                    return@launch
                } catch (e: Exception) {
                    runOnUiThread {
                        authViewModel.setError(e.localizedMessage ?: "Prepare failed")
                    }
                    return@launch
                }
                val pendingHandle = prepareResponse.pendingGetCredentialHandle
                val getResponse: GetCredentialResponse = try {
                    if (pendingHandle != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        withContext(Dispatchers.IO) {
                            credentialManager.getCredential(this@MainActivity, pendingHandle)
                        }
                    } else {
                        withContext(Dispatchers.IO) {
                            credentialManager.getCredential(this@MainActivity, getCredentialRequest)
                        }
                    }
                } catch (e: GetCredentialException) {
                    authViewModel.setError(e.localizedMessage ?: "Credential retrieval failed")
                    return@launch
                } catch (e: Exception) {
                    authViewModel.setError(e.localizedMessage ?: "Credential retrieval error")
                    return@launch
                }
                val credential = getResponse.credential
                try {
                    val type = credential.type
                    if (type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL ||
                        type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_SIWG_CREDENTIAL
                    ) {
                        val googleCred = GoogleIdTokenCredential.createFrom(credential.data)
                        val idToken = googleCred.idToken
                        if (idToken.isBlank()) {
                            authViewModel.setError("Google ID token is empty")
                            return@launch
                        }
                        authViewModel.signInWithGoogle(idToken)
                    } else {
                        authViewModel.setError("Unsupported credential type: $type")
                    }
                } catch (e: Exception) {
                    authViewModel.setError("Failed to parse credential: ${e.localizedMessage}")
                }
            } catch (e: Exception) {
                authViewModel.setError(e.localizedMessage ?: "Unexpected error in credential flow")
            }
        }
    }
}