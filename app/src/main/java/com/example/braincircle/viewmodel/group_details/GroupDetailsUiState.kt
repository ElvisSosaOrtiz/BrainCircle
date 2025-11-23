package com.example.braincircle.viewmodel.group_details

data class GroupDetailsUiState(
    val groupId: String = "",
    val name: String = "",
    val courseCode: String = "",
    val courseTitle: String = "",
    val courseDept: String = "",
    val description: String = "",
    val meetingDetails: String = "",
    val isAdmin: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String = ""
)
