package com.example.braincircle.model.service

import com.example.braincircle.model.data.ChatMessage
import com.example.braincircle.model.data.StudyGroup
import com.example.braincircle.model.data.User
import com.example.braincircle.model.response.RepositoryResponse
import kotlinx.coroutines.flow.Flow

interface FirestoreRepository {
    suspend fun listStudyGroups(): Flow<List<StudyGroup>>
    suspend fun myStudyGroups(currentUserId: String): Flow<List<StudyGroup>>
    suspend fun getStudyGroup(groupId: String): Flow<StudyGroup?>
    suspend fun createStudyGroup(group: StudyGroup): Flow<RepositoryResponse>
    suspend fun updateGroupDetails(group: StudyGroup): Flow<RepositoryResponse>
    suspend fun deleteStudyGroup(groupId: String): Flow<RepositoryResponse>
    suspend fun sendMessage(groupId: String, message: ChatMessage): Flow<RepositoryResponse>
    suspend fun getChatMessages(groupId: String): Flow<List<ChatMessage>>
    suspend fun getUserProfile(userId: String): Flow<User?>
    suspend fun createUserProfile(user: User): Flow<RepositoryResponse>
    suspend fun updateUserProfile(user: User): Flow<RepositoryResponse>
    suspend fun leaveStudyGroup(studentId: String, groupId: String): Flow<RepositoryResponse>
    suspend fun getLastMessageSent(groupId: String): Flow<ChatMessage?>
    suspend fun joinStudyGroup(studentId: String, groupId: String): Flow<RepositoryResponse>
}