package com.example.braincircle.viewmodel.chat_room

import android.net.Uri
import com.example.braincircle.model.data.ChatMessage
import java.util.Date

data class ChatRoomUiState(
    val groupId: String = "",
    val messages: List<ChatMessage> = emptyList(),
    val message: String = "",
    val senderName: String = "",
    val senderPhotoUri: Uri = Uri.EMPTY,
    val messageTimestamp: Date? = null,
    val isLoading: Boolean = false,
    val errorMessage: String = ""
)
