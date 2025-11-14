package com.example.braincircle.viewmodel.reset_password

data class ResetPasswordUiState(
    val email: String = "",
    val emailValidationMessage: String = "",
    val confirmationMessage: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String = ""
)
