package com.example.braincircle.viewmodel.sign_in

data class SignInUiState(
    val email: String = "",
    val password: String = "",
    val emailValidationMessage: String = "",
    val passwordValidationMessage: String = "",
    val errorMessage: String = "",
    val isLoading: Boolean = false,
)

