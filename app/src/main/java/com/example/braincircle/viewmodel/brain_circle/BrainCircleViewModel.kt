package com.example.braincircle.viewmodel.brain_circle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.braincircle.model.service.AuthRepository
import com.example.braincircle.view.BrainCircleScreen
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

    private val _startDestination = MutableStateFlow<String?>(null)
    val startDestination = _startDestination.asStateFlow()

    init {
        observeAuthState()
    }

    private fun observeAuthState() {
        viewModelScope.launch {
            auth.getAuthStateFlow().collect { user ->
                _uiState.update { currentState ->

                    val newName = user?.displayName ?: currentState.username
                    val newPhoto = user?.photoUrl?.toString() ?: currentState.photoUrl

                    currentState.copy(
                        username = newName,
                        photoUrl = newPhoto,
                        isUserSignedIn = (user != null)
                    )
                }
                if (user != null) {
                    if (user.displayName.isNullOrBlank()) {
                        observeAuthState()
                        return@collect
                    }
                    _startDestination.value = BrainCircleScreen.FindGroups.name
                } else {
                    _startDestination.value = BrainCircleScreen.SignIn.name
                }
            }
        }
    }

    fun updateUserProfile(username: String, photo: String) {
        _uiState.update { currentState ->
            currentState.copy(
                username = username,
                photoUrl = photo
            )
        }
    }

    fun signOut() {
        auth.signOut()
        _uiState.update { currentState ->
            currentState.copy(
                username = "",
                photoUrl = "",
                isUserSignedIn = false
            )
        }
    }
}