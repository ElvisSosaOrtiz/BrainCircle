package com.example.braincircle.viewmodel.brain_circle

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.braincircle.model.data.User
import com.example.braincircle.model.response.RepositoryResponse
import com.example.braincircle.model.service.AuthRepository
import com.example.braincircle.model.service.FirestoreRepository
import com.example.braincircle.view.BrainCircleScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BrainCircleViewModel @Inject constructor(
    private val auth: AuthRepository,
    private val firestore: FirestoreRepository
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
                if (user != null) {
                    _startDestination.value = BrainCircleScreen.FindGroups.name
                } else {
                    _startDestination.value = BrainCircleScreen.SignIn.name
                }
                _uiState.update { currentState ->

                    val newName = user?.displayName ?: currentState.username
                    val newPhoto = user?.photoUrl?.toString() ?: currentState.photoUrl

                    currentState.copy(
                        username = newName,
                        photoUrl = newPhoto,
                        isUserSignedIn = (user != null)
                    )
                }
            }
        }
    }

    fun createUserDocument(username: String, photo: String) {
        viewModelScope.launch {
            val firebaseUser = auth.currentUser()
            val user = User(
                uid = firebaseUser?.uid ?: "",
                name = firebaseUser?.displayName ?: username,
                email = firebaseUser?.email ?: "",
                photoUri = firebaseUser?.photoUrl ?: Uri.parse(photo)
            )
            firestore.createUserProfile(user)
                .catch { e ->
                    Log.e("BrainCircleViewModel", e.localizedMessage ?: "Unknown error")
                }
                .collect { response ->
                    when (response) {
                        is RepositoryResponse.Success -> {

                        }

                        is RepositoryResponse.Error -> {
                            Log.e("BrainCircleViewModel", response.message)
                        }
                    }
                }
        }
    }

    fun updateUserProfile(username: String, photo: String) {
        viewModelScope.launch {
            auth.updateUserProfile(username, Uri.parse(photo))
                .catch { e ->
                    Log.e("BrainCircleViewModel", e.localizedMessage ?: "Unknown error")
                }
                .collect {
                    _uiState.update { currentState ->
                        currentState.copy(
                            username = it?.name ?: username,
                            photoUrl = it?.photoUri?.toString() ?: photo
                        )
                    }
                }
        }
    }

    fun reloadUser() {
        viewModelScope.launch {
            val user = auth.reloadUser()
            _uiState.update { currentState ->
                currentState.copy(
                    username = user?.displayName,
                    photoUrl = user?.photoUrl?.toString(),
                    isUserSignedIn = (user != null)
                )
            }
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