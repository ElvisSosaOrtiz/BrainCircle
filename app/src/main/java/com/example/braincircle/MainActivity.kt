package com.example.braincircle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.braincircle.ui.theme.BrainCircleTheme
import com.example.braincircle.view.BrainCircleApp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        var keepOn = true
        splashScreen.setKeepOnScreenCondition { keepOn }
        setContent {
            BrainCircleTheme {
                BrainCircleApp()
            }
        }
        lifecycleScope.launch {
            delay(700)
            keepOn = false
        }
    }

//    private fun beginCredentialManagerSignIn() {
//        lifecycleScope.launch {
//            try {
//                val prepareResponse: PrepareGetCredentialResponse = try {
//                    withContext(Dispatchers.IO) {
//                        credentialManager.prepareGetCredential(getCredentialRequest)
//                    }
//                } catch (e: GetCredentialException) {
//                    runOnUiThread {
//                        authViewModel.setError(e.localizedMessage ?: "No credentials available")
//                    }
//                    return@launch
//                } catch (e: Exception) {
//                    runOnUiThread {
//                        authViewModel.setError(e.localizedMessage ?: "Prepare failed")
//                    }
//                    return@launch
//                }
//                val pendingHandle = prepareResponse.pendingGetCredentialHandle
//                val getResponse: GetCredentialResponse = try {
//                    if (pendingHandle != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
//                        withContext(Dispatchers.IO) {
//                            credentialManager.getCredential(this@MainActivity, pendingHandle)
//                        }
//                    } else {
//                        withContext(Dispatchers.IO) {
//                            credentialManager.getCredential(this@MainActivity, getCredentialRequest)
//                        }
//                    }
//                } catch (e: GetCredentialException) {
//                    authViewModel.setError(e.localizedMessage ?: "Credential retrieval failed")
//                    return@launch
//                } catch (e: Exception) {
//                    authViewModel.setError(e.localizedMessage ?: "Credential retrieval error")
//                    return@launch
//                }
//                val credential = getResponse.credential
//                try {
//                    val type = credential.type
//                    if (type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL ||
//                        type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_SIWG_CREDENTIAL
//                    ) {
//                        val googleCred = GoogleIdTokenCredential.createFrom(credential.data)
//                        val idToken = googleCred.idToken
//                        if (idToken.isBlank()) {
//                            authViewModel.setError("Google ID token is empty")
//                            return@launch
//                        }
//                        authViewModel.signInWithGoogle(idToken)
//                    } else {
//                        authViewModel.setError("Unsupported credential type: $type")
//                    }
//                } catch (e: Exception) {
//                    authViewModel.setError("Failed to parse credential: ${e.localizedMessage}")
//                }
//            } catch (e: Exception) {
//                authViewModel.setError(e.localizedMessage ?: "Unexpected error in credential flow")
//            }
//        }
//    }
}