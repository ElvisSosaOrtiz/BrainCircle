package com.example.braincircle.viewmodel.chat_room

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.braincircle.model.data.ChatMessage
import com.example.braincircle.model.service.AuthRepository
import com.example.braincircle.model.service.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatRoomViewModel @Inject constructor(
    val firestore: FirestoreRepository,
    val auth: AuthRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val groupId: String = checkNotNull(savedStateHandle["groupId"])

    private val _uiState = MutableStateFlow(ChatRoomUiState())
    val uiState: StateFlow<ChatRoomUiState> = _uiState.asStateFlow()

    init {
        getChatMessages(groupId)
    }

    private fun getChatMessages(id: String) {
        _uiState.update { currentState ->
            currentState.copy(
                errorMessage = "",
                isLoading = true
            )
        }
        viewModelScope.launch {
            firestore.getChatMessages(id)
                .catch { e ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            errorMessage = e.localizedMessage ?: "Error fetching messages",
                            isLoading = false
                        )
                    }
                }
                .collect { messages ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            senderId = auth.currentUser()!!.uid,
                            messages = messages,
                            isLoading = false
                        )
                    }
                }
        }
    }

    fun onMessageChange(newValue: String) {
        _uiState.update { currentState ->
            currentState.copy(message = newValue)
        }
    }

    fun sendMessage() {
        _uiState.update { currentState ->
            currentState.copy(
                errorMessage = "",
                isLoading = true
            )
        }
        viewModelScope.launch {
            uiState.value.apply {
                val chatMessage = ChatMessage(
                    groupId = this@ChatRoomViewModel.groupId,
                    senderId = auth.currentUser()!!.uid,
                    senderName = auth.currentUser()!!.displayName!!,
                    senderPhotoUri = auth.currentUser()!!.photoUrl.toString(),
                    text = message
                )
                firestore.sendMessage(groupId, chatMessage)
                    .catch { e ->
                        _uiState.update { currentState ->
                            currentState.copy(
                                errorMessage = e.localizedMessage ?: "Error sending message",
                                isLoading = false
                            )
                        }
                    }
                    .collect {
                        _uiState.update { currentState ->
                            currentState.copy(
                                message = "",
                                isLoading = false
                            )
                        }
                    }
            }
        }
    }
}