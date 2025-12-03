package com.example.braincircle.viewmodel.create_group

import android.net.Uri
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
class CreateGroupViewModel @Inject constructor(
    val firestore: FirestoreRepository,
    val auth: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateGroupUiState())
    val uiState: StateFlow<CreateGroupUiState> = _uiState.asStateFlow()

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

    fun createGroup(navToMyGroups: () -> Unit) {
        _uiState.update { currentState ->
            currentState.copy(
                errorMessage = "",
                isLoading = true
            )
        }
        viewModelScope.launch {
            uiState.value.apply {
                val uid = auth.currentUser()!!.uid
                val studyGroup = StudyGroup(
                    adminId = uid,
                    name = name,
                    courseCode = courseCode,
                    courseTitle = courseTitle,
                    courseDept = courseDept,
                    description = description,
                    locationName = locationName,
                    locationLink = locationLink.toString(),
                    meetingDate = meetingDate,
                    members = listOf(uid)
                )
                firestore.createStudyGroup(studyGroup)
                    .catch { e ->
                        _uiState.update { currentState ->
                            currentState.copy(
                                errorMessage = e.localizedMessage ?: "Error creating study group",
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