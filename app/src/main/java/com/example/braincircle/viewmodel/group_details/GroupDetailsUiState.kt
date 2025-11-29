package com.example.braincircle.viewmodel.group_details

import android.net.Uri
import java.util.Date

data class GroupDetailsUiState(
    val groupId: String = "",
    val name: String = "",
    val courseCode: String = "",
    val courseTitle: String = "",
    val courseDept: String = "",
    val description: String = "",
    val locationName: String = "",
    val locationLink: Uri = Uri.EMPTY,
    val meetingDate: Date? = null,
    val isAdmin: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String = ""
)
