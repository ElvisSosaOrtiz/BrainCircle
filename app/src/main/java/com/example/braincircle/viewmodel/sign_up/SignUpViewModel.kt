package com.example.braincircle.viewmodel.sign_up

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.braincircle.model.AuthResponse
import com.example.braincircle.model.service.AuthRepository
import com.example.braincircle.view.common.isValidEmail
import com.example.braincircle.view.common.isValidPassword
import com.example.braincircle.view.common.passwordMatches
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val auth: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    private val email
        get() = uiState.value.email
    private val password
        get() = uiState.value.password
    private val username
        get() = uiState.value.username
    private val photo
        get() = uiState.value.photo

    fun onEmailChange(newValue: String) {
        _uiState.update { currentState ->
            currentState.copy(email = newValue)
        }
    }

    fun onUsernameChange(newValue: String) {
        _uiState.update { currentState ->
            currentState.copy(username = newValue)
        }
    }

    fun onPasswordChange(newValue: String) {
        _uiState.update { currentState ->
            currentState.copy(password = newValue)
        }
    }

    fun onRepeatPasswordChange(newValue: String) {
        _uiState.update { currentState ->
            currentState.copy(repeatPassword = newValue)
        }
    }

    fun signUp(navigateToFindGroups: () -> Unit) {
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

        if (username.isEmpty()) {
            _uiState.update { currentState ->
                currentState.copy(
                    usernameValidationMessage = "Username cannot be empty",
                    isLoading = false
                )
            }
            return
        }

        if (!password.isValidPassword()) {
            _uiState.update { currentState ->
                currentState.copy(
                    passwordValidationMessage = "Your password should be at least 9 characters long and contain one uppercase letter, one lowercase letter, one digit and a special character, with no whitespaces",
                    isLoading = false
                )
            }
            return
        }

        if (!password.passwordMatches(_uiState.value.repeatPassword)) {
            _uiState.update { currentState ->
                currentState.copy(
                    repeatPasswordValidationMessage = "Passwords do not match",
                    isLoading = false
                )
            }
            return
        }

        viewModelScope.launch {
            auth.signUpWithEmail(email, password, username, photo)
                .catch { e ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            errorMessage = e.localizedMessage ?: "Account creation failed",
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
                usernameValidationMessage = "",
                passwordValidationMessage = "",
                repeatPasswordValidationMessage = "",
                errorMessage = "",
                isLoading = isLoading
            )
        }
    }
}