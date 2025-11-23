package com.example.braincircle.viewmodel.find_groups

import com.example.braincircle.model.data.StudyGroup

data class FindGroupsUiState(
    val groups: List<StudyGroup> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String = ""
)