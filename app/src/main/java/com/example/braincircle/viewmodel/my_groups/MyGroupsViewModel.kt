package com.example.braincircle.viewmodel.my_groups

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.braincircle.model.service.AuthRepository
import com.example.braincircle.model.service.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class MyGroupsViewModel @Inject constructor(
    private val firestore: FirestoreRepository,
    private val auth: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyGroupsUiState())
    val uiState: StateFlow<MyGroupsUiState> = _uiState.asStateFlow()

    init {
        myGroups()
    }

    private fun myGroups() {
        _uiState.update { currentState ->
            currentState.copy(
                isLoading = true
            )
        }
        viewModelScope.launch {
            firestore.myStudyGroups(auth.currentUser()!!.uid)
                .catch { e ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            errorMessage = e.localizedMessage ?: "Error fetching my groups",
                            isLoading = false
                        )
                    }
                }
                .collect { groups ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            myGroups = groups,
                            isLoading = false
                        )
                    }
                }
        }
    }

    fun getLastMessageSent(groupId: String) {
        viewModelScope.launch {
            firestore.getLastMessageSent(groupId)
                .catch { e ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            errorMessage = e.localizedMessage ?: "Error fetching last message sent",
                            isLoading = false
                        )
                    }
                }
                .collect { message ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            messageSenderName = message?.senderName ?: "",
                            lastMessageSent = message?.text ?: "",
                            lastMessageSentTime = SimpleDateFormat("HH:mm").format(message?.timestamp ?: Date()),
                            isCurrentUser = auth.currentUser()!!.uid == message?.senderId,
                            isLoading = false
                        )
                    }
                }
        }
    }
}