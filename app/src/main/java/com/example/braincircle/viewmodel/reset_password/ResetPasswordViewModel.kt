package com.example.braincircle.viewmodel.reset_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.braincircle.model.AuthResponse
import com.example.braincircle.model.service.AuthRepository
import com.example.braincircle.view.common.isValidEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val auth: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ResetPasswordUiState())
    val uiState: StateFlow<ResetPasswordUiState> = _uiState.asStateFlow()

    private val email
        get() = uiState.value.email

    fun onEmailChange(newValue: String) {
        _uiState.update { currentState ->
            currentState.copy(email = newValue)
        }
    }

    fun resetPassword() {
        clearMessages(true)

        if (!email.isValidEmail()) {
            _uiState.update { currentState ->
                currentState.copy(
                    emailValidationMessage = "Invalid email or empty",
                    isLoading = false
                )
            }
            return
        }

        viewModelScope.launch {
            auth.sendPasswordReset(email)
                .catch { e ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            errorMessage = e.localizedMessage ?: "Password reset failed",
                            isLoading = false
                        )
                    }
                }
                .collect { response ->
                    when (response) {
                        is AuthResponse.Success -> {
                            _uiState.update { currentState ->
                                currentState.copy(
                                    confirmationMessage = "Password reset request sent. Check your email.",
                                    isLoading = false
                                )
                            }
                        }
                        is AuthResponse.Error -> {
                            _uiState.update { currentState ->
                                currentState.copy(
                                    errorMessage = response.message,
                                    isLoading = false
                                )
                            }
                        }
                    }
                }
        }
        clearFields()
    }

    fun clearMessages(isLoading: Boolean = false) {
        _uiState.update { currentState ->
            currentState.copy(
                emailValidationMessage = "",
                errorMessage = "",
                isLoading = isLoading
            )
        }
    }

    fun clearFields() {
        _uiState.update { currentState ->
            currentState.copy(email = "")
        }
    }
}