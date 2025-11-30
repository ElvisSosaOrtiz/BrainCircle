package com.example.braincircle.viewmodel.create_group

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
import javax.inject.Inject

@HiltViewModel
class CreateGroupViewModel @Inject constructor(
    val firestore: FirestoreRepository,
    val auth: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateGroupUiState())
    val uiState: StateFlow<CreateGroupUiState> = _uiState.asStateFlow()

    fun createGroup(navToMyGroups: () -> Unit) {
        _uiState.update { currentState ->
            currentState.copy(
                errorMessage = "",
                isLoading = true
            )
        }
        viewModelScope.launch {
            uiState.value.apply {
                val studyGroup = StudyGroup(
                    adminId = auth.currentUser()!!.uid,
                    name = name,
                    courseCode = courseCode,
                    courseTitle = courseTitle,
                    courseDept = courseDept,
                    description = description,
                    locationName = meetingDetails,
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
}