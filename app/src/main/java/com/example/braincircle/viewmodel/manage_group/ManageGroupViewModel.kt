package com.example.braincircle.viewmodel.manage_group

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.braincircle.model.data.StudyGroup
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
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ManageGroupViewModel @Inject constructor(
    val firestore: FirestoreRepository,
    val auth: AuthRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val groupId: String = checkNotNull(savedStateHandle["groupId"])

    private val _uiState = MutableStateFlow(ManageGroupUiState())
    val uiState: StateFlow<ManageGroupUiState> = _uiState.asStateFlow()

    init {
        getGroup(groupId)
    }

    private fun getGroup(id: String) {
        _uiState.update { currentState ->
            currentState.copy(
                errorMessage = "",
                isLoading = true
            )
        }
        viewModelScope.launch {
            firestore.getStudyGroup(id)
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
                                locationName = group.locationName,
                                locationLink = Uri.parse(group.locationLink),
                                meetingDate = group.meetingDate,
                                isAdmin = group.adminId == auth.currentUser()!!.uid,
                                isLoading = false
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

    fun onNameChange(name: String) {
        _uiState.update { currentState ->
            currentState.copy(name = name)
        }
    }

    fun onCourseCodeChange(courseCode: String) {
        _uiState.update { currentState ->
            currentState.copy(courseCode = courseCode)
        }
    }

    fun onCourseTitleChange(courseTitle: String) {
        _uiState.update { currentState ->
            currentState.copy(courseTitle = courseTitle)
        }
    }

    fun onCourseDeptChange(courseDept: String) {
        _uiState.update { currentState ->
            currentState.copy(courseDept = courseDept)
        }
    }

    fun onDescriptionChange(description: String) {
        _uiState.update { currentState ->
            currentState.copy(description = description)
        }
    }

    fun onLocationNameChange(locationName: String) {
        _uiState.update { currentState ->
            currentState.copy(locationName = locationName)
        }
    }

    fun onLocationLinkChange(locationLink: String) {
        _uiState.update { currentState ->
            currentState.copy(locationLink = Uri.parse(locationLink))
        }
    }

    fun onMeetingDateChange(meetingDate: Date) {
        _uiState.update { currentState ->
            currentState.copy(meetingDate = meetingDate)
        }
    }

    fun updateGroup(navToGroupDetails: (String) -> Unit) {
        _uiState.update { currentState ->
            currentState.copy(
                errorMessage = "",
                isLoading = true
            )
        }
        if (_uiState.value.isAdmin) {
            viewModelScope.launch {
                uiState.value.apply {
                    val group = StudyGroup(
                        groupId = groupId,
                        name = name,
                        courseCode = courseCode,
                        courseTitle = courseTitle,
                        courseDept = courseDept,
                        description = description,
                        locationName = locationName,
                        locationLink = locationLink.toString(),
                        meetingDate = meetingDate
                    )
                    firestore.updateGroupDetails(group)
                        .catch { e ->
                            _uiState.update { currentState ->
                                currentState.copy(
                                    errorMessage = e.localizedMessage ?: "Error updating group",
                                    isLoading = false
                                )
                            }
                        }
                        .collect { response ->
                            when (response) {
                                is RepositoryResponse.Success -> {
                                    navToGroupDetails(_uiState.value.name)
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
        } else {
            _uiState.update { currentState ->
                currentState.copy(
                    errorMessage = "You are not the admin of this group",
                    isLoading = false
                )
            }
        }
    }

    fun removeGroup(navToGroupDetails: (String) -> Unit) {
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
                                navToGroupDetails(_uiState.value.name)
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

    fun clearMessages() {
        _uiState.update { currentState ->
            currentState.copy(
                errorMessage = "",
                isLoading = false
            )
        }
    }
}