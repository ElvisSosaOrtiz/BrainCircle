package com.example.braincircle.viewmodel.sign_in

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.braincircle.model.AuthResponse
import com.example.braincircle.model.service.AuthRepository
import com.example.braincircle.view.common.isValidEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val auth: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignInUiState())
    val uiState: StateFlow<SignInUiState> = _uiState.asStateFlow()

    private val email
        get() = uiState.value.email

    private val password
        get() = uiState.value.password

    fun onEmailChange(newValue: String) {
        _uiState.update { currentState ->
            currentState.copy(email = newValue)
        }
    }

    fun onPasswordChange(newValue: String) {
        _uiState.update { currentState ->
            currentState.copy(password = newValue)
        }
    }

    fun signInWithEmailAndPassword(navigateToFindGroups: () -> Unit) {
        clearMessages(isLoading = true)

        if (!email.isValidEmail()) {
            _uiState.update { currentState ->
                currentState.copy(
                    emailValidationMessage = "Invalid email or empty",
                    isLoading = false
                )
            }
            return
        }

        if (password.isBlank()) {
            _uiState.update { currentState ->
                currentState.copy(
                    passwordValidationMessage = "Password cannot be blank",
                    isLoading = false
                )
            }
            return
        }

        viewModelScope.launch {
            auth.signInWithEmail(email, password)
                .catch { e ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            errorMessage = e.localizedMessage ?: "Sign in failed",
                            isLoading = false
                        )
                    }
                }
                .collect { response ->
                    when (response) {
                        is AuthResponse.Success -> {
                            navigateToFindGroups()
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

    fun signInWithGoogle(
        @ApplicationContext context: Context,
        navigateToFindGroups: () -> Unit
    ) {
        clearMessages(isLoading = true)

        viewModelScope.launch {
            auth.signInWithGoogle(context)
                .catch { e ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            errorMessage = e.localizedMessage ?: "Sign in failed",
                            isLoading = false
                        )
                    }
                }
                .collect { response ->
                    when (response) {
                        is AuthResponse.Success -> {
                            navigateToFindGroups()
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
    }

    fun clearMessages(isLoading: Boolean = false) {
        _uiState.update { currentState ->
            currentState.copy(
                emailValidationMessage = "",
                passwordValidationMessage = "",
                errorMessage = "",
                isLoading = isLoading
            )
        }
    }

    fun clearFields() {
        _uiState.update { currentState ->
            currentState.copy(
                email = "",
                password = ""
            )
        }
    }
}