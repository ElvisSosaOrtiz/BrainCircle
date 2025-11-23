package com.example.braincircle.viewmodel.my_groups

import com.example.braincircle.model.data.StudyGroup

data class MyGroupsUiState(
    val myGroups: List<StudyGroup> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String = ""
)
