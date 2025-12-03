package com.example.braincircle.viewmodel.manage_group

import android.net.Uri
import java.util.Date

data class ManageGroupUiState(
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
