package com.example.braincircle.model.service

import com.example.braincircle.model.data.ChatMessage
import com.example.braincircle.model.data.StudyGroup
import com.example.braincircle.model.data.User
import kotlinx.coroutines.flow.Flow

interface FirestoreRepository {
    suspend fun createStudyGroup(adminUser: User, groupName: String, courseId: String)
    suspend fun updateGroupDetails(group: StudyGroup, currentUserId: String)
    suspend fun deleteStudyGroup(groupId: String, currentUserId: String)
    suspend fun sendMessage(groupId: String, message: ChatMessage)
    suspend fun getChatMessagesFlow(groupId: String): Flow<List<ChatMessage>>
}