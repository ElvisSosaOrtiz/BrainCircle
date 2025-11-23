package com.example.braincircle.viewmodel.find_groups

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class FindGroupsViewModel @Inject constructor(
    private val firestore: FirestoreRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FindGroupsUiState())
    val uiState: StateFlow<FindGroupsUiState> = _uiState.asStateFlow()

    init {
        listGroups()
    }

    private fun listGroups() {
        _uiState.update { currentState ->
            currentState.copy(
                errorMessage = "",
                isLoading = true
            )
        }
        viewModelScope.launch {
            firestore.listStudyGroups()
                .catch { e ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            errorMessage = e.localizedMessage ?: "Error fetching groups",
                            isLoading = false
                        )
                    }
                }
                .collect { groups ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            groups = groups,
                            isLoading = false
                        )
                    }
                }
        }
    }
}