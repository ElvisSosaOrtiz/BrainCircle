package com.example.braincircle.model.implementation

import com.example.braincircle.model.data.ChatMessage
import com.example.braincircle.model.data.StudyGroup
import com.example.braincircle.model.data.User
import com.example.braincircle.model.service.FirestoreRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

private const val GROUP_LISTING = "groupListing"
private const val GROUP = "group"

class FirestoreRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
) : FirestoreRepository {

    override suspend fun createStudyGroup(
        adminUser: User,
        groupName: String,
        courseId: String
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun updateGroupDetails(group: StudyGroup, currentUserId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteStudyGroup(groupId: String, currentUserId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun sendMessage(
        groupId: String,
        message: ChatMessage
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun getChatMessagesFlow(groupId: String): Flow<List<ChatMessage>> {
        TODO("Not yet implemented")
    }
}