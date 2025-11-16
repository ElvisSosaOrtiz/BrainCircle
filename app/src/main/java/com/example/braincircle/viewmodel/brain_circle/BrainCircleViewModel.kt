package com.example.braincircle.viewmodel.brain_circle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.braincircle.model.service.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BrainCircleViewModel @Inject constructor(
    private val auth: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BrainCircleUiState())
    val uiState: StateFlow<BrainCircleUiState> = _uiState.asStateFlow()

    init {
        observeAuthState()
    }

    private fun observeAuthState() {
        viewModelScope.launch {
            auth.getAuthStateFlow().collect { user ->
                _uiState.update { currentState ->
                    currentState.copy(
                        username = user?.displayName,
                        photoUrl = user?.photoUrl?.toString(),
                        isUserSignedIn = (user != null)
                    )
                }
            }
        }
    }

    fun signOut() {
        auth.signOut()
//        _uiState.update { currentState ->
//            currentState.copy(isUserSignedIn = false)
//        }
    }
}