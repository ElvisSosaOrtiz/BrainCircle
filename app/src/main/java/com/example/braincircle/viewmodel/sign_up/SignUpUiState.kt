package com.example.braincircle.viewmodel.sign_up

data class SignUpUiState(
    val email: String = "",
    val password: String = "",
    val repeatPassword: String = "",
    val emailValidationMessage: String = "",
    val passwordValidationMessage: String = "",
    val repeatPasswordValidationMessage: String = "",
    val errorMessage: String = "",
    val isLoading: Boolean = false
)
