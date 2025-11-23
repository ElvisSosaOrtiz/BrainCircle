package com.example.braincircle.viewmodel.group_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.braincircle.model.response.RepositoryResponse
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
class GroupDetailsViewModel @Inject constructor(
    private val firestore: FirestoreRepository,
    private val auth: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GroupDetailsUiState())
    val uiState: StateFlow<GroupDetailsUiState> = _uiState.asStateFlow()

    init {
        getGroupDetails()
    }

    private fun getGroupDetails() {
        _uiState.update { currentState ->
            currentState.copy(
                errorMessage = "",
                isLoading = true
            )
        }
        viewModelScope.launch {
            firestore.getStudyGroup(_uiState.value.groupId)
                .catch { e ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            errorMessage = e.localizedMessage ?: "Error fetching group",
                            isLoading = false
                        )
                    }
                }
                .collect { group ->
                    if (group != null) {
                        _uiState.update { currentState ->
                            currentState.copy(
                                groupId = group.groupId,
                                name = group.name,
                                courseCode = group.courseCode,
                                courseTitle = group.courseTitle,
                                courseDept = group.courseDept,
                                description = group.description,
                                meetingDetails = group.meetingDetails,
                                isAdmin = group.adminId == auth.currentUser()!!.uid,
                                isLoading = false,
                            )
                        }
                    } else {
                        _uiState.update { currentState ->
                            currentState.copy(
                                errorMessage = "Group not found",
                                isLoading = false
                            )
                        }
                    }
                }
        }
    }

    fun leaveGroup(navToMyGroups: () -> Unit) {
        _uiState.update { currentState ->
            currentState.copy(
                errorMessage = "",
                isLoading = true
            )
        }
        viewModelScope.launch {
            firestore.leaveStudyGroup(auth.currentUser()!!.uid, _uiState.value.groupId)
                .catch { e ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            errorMessage = e.localizedMessage ?: "Error leaving group",
                            isLoading = false
                        )
                    }
                }
                .collect { response ->
                    when (response) {
                        is RepositoryResponse.Success -> {
                            navToMyGroups()
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
    }

    fun removeGroup(navToMyGroups: () -> Unit) {
        _uiState.update { currentState ->
            currentState.copy(
                errorMessage = "",
                isLoading = true
            )
        }
        if (_uiState.value.isAdmin) {
            viewModelScope.launch {
                firestore.deleteStudyGroup(_uiState.value.groupId)
                    .catch { e ->
                        _uiState.update { currentState ->
                            currentState.copy(
                                errorMessage = e.localizedMessage ?: "Error deleting group",
                                isLoading = false
                            )
                        }
                    }
                    .collect { response ->
                        when (response) {
                            is RepositoryResponse.Success -> {
                                navToMyGroups()
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
        } else {
            _uiState.update { currentState ->
                currentState.copy(
                    errorMessage = "You are not the admin of this group",
                    isLoading = false
                )
            }
        }
    }
}