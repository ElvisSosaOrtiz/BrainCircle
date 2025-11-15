package com.example.braincircle.viewmodel.brain_circle

import androidx.lifecycle.ViewModel
import com.example.braincircle.model.service.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class BrainCircleViewModel @Inject constructor(
    private val auth: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BrainCircleUiState())
    val uiState: StateFlow<BrainCircleUiState> = _uiState.asStateFlow()

    val username: String
        get() = auth.currentUser()?.displayName ?: ""

    init {
        _uiState.update { currentState ->
            currentState.copy(isUserSignedIn = auth.isUserSignedIn())
        }
    }

    fun signOut() {
        auth.signOut()
        _uiState.update { currentState ->
            currentState.copy(isUserSignedIn = false)
        }
    }
}