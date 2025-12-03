package com.example.braincircle.view

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.braincircle.R
import com.example.braincircle.view.common.EmailField
import com.example.braincircle.view.common.PasswordField
import com.example.braincircle.viewmodel.sign_in.SignInViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    onSignInClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onSignInWithGoogleClick: () -> Unit,
    onResetPasswordClick: () -> Unit,
    viewModel: SignInViewModel = hiltViewModel(),
    topBar: @Composable () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = topBar
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.primaryContainer),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = Modifier.size(150.dp),
                painter = painterResource(R.drawable.brain_circle_app_icon),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimaryContainer)
            )
            Spacer(modifier = Modifier.height(32.dp))
            EmailField(
                value = uiState.email,
                nextIsPassword = true,
                enabled = !uiState.isLoading,
                onValueChange = { viewModel.onEmailChange(it) }
            )
            if (uiState.emailValidationMessage.isNotEmpty()) {
                Text(
                    text = uiState.emailValidationMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(Modifier.padding(vertical = 8.dp))
            PasswordField(
                value = uiState.password,
                nextIsPasswordRepeat = false,
                label = R.string.password,
                enabled = !uiState.isLoading,
                onValueChange = { viewModel.onPasswordChange(it) }
            )
            if (uiState.passwordValidationMessage.isNotEmpty()) {
                   Text(
                        text = uiState.passwordValidationMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Row(Modifier.padding(top = 8.dp)) {
                Text(
                    text = stringResource(R.string.forgot_password).uppercase(),
                    color = MaterialTheme.colorScheme.outline,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    modifier = Modifier.clickable(onClick = onResetPasswordClick),
                    text = stringResource(R.string.reset_here).uppercase(),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(Modifier.padding(vertical = 16.dp))
            FilledTonalButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 48.dp),
                colors = ButtonDefaults.filledTonalButtonColors(MaterialTheme.colorScheme.onPrimaryContainer),
                elevation = ButtonDefaults.filledTonalButtonElevation(8.dp),
                enabled = !uiState.isLoading,
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                onClick = { viewModel.signInWithEmailAndPassword(onSignInClick) }
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator()
                } else {
                    Text(
                        text = stringResource(R.string.sign_in),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 48.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onPrimaryContainer),
                enabled = !uiState.isLoading,
                onClick = onSignUpClick
            ) {
                Text(
                    text = stringResource(R.string.sign_up),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Spacer(Modifier.padding(vertical = 16.dp))
            Text(
                text = stringResource(R.string.or_connect_with).uppercase(),
                color = MaterialTheme.colorScheme.outline,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(Modifier.padding(vertical = 16.dp))
            FilledTonalButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 48.dp),
                colors = ButtonDefaults.filledTonalButtonColors(MaterialTheme.colorScheme.onPrimaryContainer),
                elevation = ButtonDefaults.filledTonalButtonElevation(8.dp),
                enabled = !uiState.isLoading,
                onClick = { viewModel.signInWithGoogle(context, onSignInWithGoogleClick) }
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(R.drawable.google__g__logo),
                        contentDescription = null
                    )
                    Spacer(Modifier.padding(horizontal = 4.dp))
                    Text(
                        text = stringResource(R.string.google),
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
        LaunchedEffect(uiState.errorMessage) {
            if (uiState.errorMessage.isNotEmpty()) {
                scope.launch {
                    snackBarHostState.showSnackbar(
                        message = uiState.errorMessage,
                        actionLabel = "Dismiss",
                        duration = SnackbarDuration.Short
                    )
                }
                viewModel.clearMessages()
            }
        }
    }
}