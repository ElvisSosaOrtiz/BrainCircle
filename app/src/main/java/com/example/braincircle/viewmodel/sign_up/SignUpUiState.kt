package com.example.braincircle.viewmodel.sign_up

import android.net.Uri

data class SignUpUiState(
    val email: String = "",
    val password: String = "",
    val username: String = "",
    val photo: Uri? = null,
    val repeatPassword: String = "",
    val emailValidationMessage: String = "",
    val usernameValidationMessage: String = "",
    val passwordValidationMessage: String = "",
    val repeatPasswordValidationMessage: String = "",
    val errorMessage: String = "",
    val isLoading: Boolean = false
)
