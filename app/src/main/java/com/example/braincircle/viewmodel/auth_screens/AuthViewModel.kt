package com.example.braincircle.viewmodel.auth_screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.braincircle.model.service.AuthRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val repo: AuthRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun checkCurrentUser() {
        repo.currentUser()?.let {
            _uiState.value = AuthUiState.Success(it)
        } ?: run {
            _uiState.value = AuthUiState.Idle
        }
    }

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val res = repo.signInWithGoogle(idToken)
            handleResult(res)
        }
    }

    fun signUpWithEmail(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val res = repo.signUpWithEmail(email.trim(), password)
            handleResult(res)
        }
    }

    fun signInWithEmail(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val res = repo.signInWithEmail(email.trim(), password)
            handleResult(res)
        }
    }

    fun signOut() {
        repo.signOut()
        _uiState.value = AuthUiState.Idle
    }

    private fun handleResult(result: Result<FirebaseUser?>) {
        if (result.isSuccess) {
            val user = result.getOrNull()
            if (user != null) _uiState.value = AuthUiState.Success(user)
            else _uiState.value = AuthUiState.Error("Authentication succeeded but user is null")
        } else {
            _uiState.value = AuthUiState.Error(result.exceptionOrNull()?.localizedMessage ?: "Authentication failed")
        }
    }

    fun setError(message: String) {
        _uiState.value = AuthUiState.Error(message)
    }

    fun clearMessages() {
        _uiState.value = AuthUiState.Idle
    }
}