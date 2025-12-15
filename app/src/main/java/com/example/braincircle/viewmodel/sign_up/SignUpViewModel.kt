package com.example.braincircle.viewmodel.sign_up

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.braincircle.model.data.User
import com.example.braincircle.model.response.RepositoryResponse
import com.example.braincircle.model.service.AuthRepository
import com.example.braincircle.model.service.FirestoreRepository
import com.example.braincircle.model.service.UserService
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
    private val auth: AuthRepository,
    private val firestore: FirestoreRepository,
    private val userService: UserService
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
            userService.createUserFromAuth(email, password, username, photo)
                .catch { e ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            errorMessage = e.localizedMessage ?: "Account creation failed",
                            isLoading = false
                        )
                    }
                }
                .collect { response ->
                    if (response is RepositoryResponse.Success) {
                        navigateToFindGroups()
                    } else if (response is RepositoryResponse.Error) {
                        _uiState.update { currentState ->
                            currentState.copy(
                                errorMessage = response.message,
                                isLoading = false
                            )
                        }
                    }
                }

//            auth.signUpWithEmail(email, password, username, photo)
//                .catch { e ->
//                    _uiState.update { currentState ->
//                        currentState.copy(
//                            errorMessage = e.localizedMessage ?: "Account creation failed",
//                            isLoading = false
//                        )
//                    }
//                }
//                .collect { response ->
//                    if (response != null) {
//                        val user = User(
//                            uid = response.uid,
//                            name = username,
//                            email = email,
//                            photoUri = photo
//                        )
//                        createUserDocument(user, navigateToFindGroups)
//                    } else {
//                        _uiState.update { currentState ->
//                            currentState.copy(
//                                errorMessage = "Account creation failed",
//                                isLoading = false
//                            )
//                        }
//                    }
//                }
        }
        clearFields()
    }

    private suspend fun createUserDocument(user: User, navigateToFindGroups: () -> Unit) {
        firestore.createUserProfile(user)
            .catch { e ->
                _uiState.update { currentState ->
                    currentState.copy(
                        errorMessage = e.localizedMessage ?: "Error creating user document",
                        isLoading = false
                    )
                }
            }
            .collect { response ->
                when (response) {
                    is RepositoryResponse.Success -> {
                        navigateToFindGroups()
                    }

                    is RepositoryResponse.Error -> {
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

    fun clearFields() {
        _uiState.update { currentState ->
            currentState.copy(
                email = "",
                password = "",
                repeatPassword = "",
                username = "",
                photo = Uri.EMPTY
            )
        }
    }
}