package com.example.braincircle.viewmodel.my_groups

import com.example.braincircle.model.data.StudyGroup

data class MyGroupsUiState(
    val myGroups: List<StudyGroup> = emptyList(),
    val messageSenderName: String = "",
    val lastMessageSent: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String = ""
)
