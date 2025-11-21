package com.example.braincircle.view

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.braincircle.R
import com.example.braincircle.view.common.EmailField
import com.example.braincircle.view.common.PasswordField
import com.example.braincircle.viewmodel.sign_up.SignUpViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    onCreateAccountClick: () -> Unit,
    onBackToSignInClick: () -> Unit,
    viewModel: SignUpViewModel = hiltViewModel(),
    topBar: @Composable () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = topBar
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
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
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 48.dp),
                shape = RoundedCornerShape(50.dp),
                singleLine = true,
                enabled = !uiState.isLoading,
                value = uiState.username,
                onValueChange = { viewModel.onUsernameChange(it) },
                label = { Text(stringResource(R.string.username)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = stringResource(R.string.username)
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )
            if (uiState.usernameValidationMessage.isNotEmpty()) {
                Text(
                    text = uiState.usernameValidationMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(Modifier.padding(vertical = 8.dp))
            PasswordField(
                value = uiState.password,
                nextIsPasswordRepeat = true,
                enabled = !uiState.isLoading,
                label = R.string.password,
                onValueChange = { viewModel.onPasswordChange(it) }
            )
            if (uiState.passwordValidationMessage.isNotEmpty()) {
                Text(
                    modifier = Modifier.padding(horizontal = 48.dp),
                    text = uiState.passwordValidationMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(Modifier.padding(vertical = 8.dp))
            PasswordField(
                value = uiState.repeatPassword,
                nextIsPasswordRepeat = false,
                enabled = !uiState.isLoading,
                label = R.string.repeat_password,
                onValueChange = { viewModel.onRepeatPasswordChange(it) }
            )
            if (uiState.repeatPasswordValidationMessage.isNotEmpty()) {
                Text(
                    text = uiState.repeatPasswordValidationMessage,
                    color = MaterialTheme.colorScheme.error,
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
                onClick = { viewModel.signUp(onCreateAccountClick) }
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator()
                } else {
                    Text(
                        text = stringResource(R.string.create_account),
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
                onClick = onBackToSignInClick
            ) {
                Text(
                    text = stringResource(R.string.i_have_an_account),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
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