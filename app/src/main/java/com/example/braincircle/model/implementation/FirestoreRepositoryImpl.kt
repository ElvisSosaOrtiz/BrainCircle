package com.example.braincircle.model.implementation

import android.util.Log
import com.example.braincircle.model.data.ChatMessage
import com.example.braincircle.model.data.StudyGroup
import com.example.braincircle.model.data.User
import com.example.braincircle.model.response.RepositoryResponse
import com.example.braincircle.model.service.FirestoreRepository
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

private const val TAG = "FirestoreRepositoryImpl"
private const val STUDY_GROUPS_COLLECTION = "studyGroups"
private const val MESSAGES_COLLECTION = "messages"
private const val USERS_COLLECTION = "users"

class FirestoreRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : FirestoreRepository {

    override suspend fun createStudyGroup(group: StudyGroup): Flow<RepositoryResponse> =
        callbackFlow {
            firestore.collection(STUDY_GROUPS_COLLECTION)
                .add(group)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        firestore.collection(USERS_COLLECTION)
                            .document(group.adminId)
                            .update(User::myGroups.name, FieldValue.arrayUnion(group.groupId))
                            .addOnCompleteListener { updateTask ->
                                if (updateTask.isSuccessful) {
                                    trySend(RepositoryResponse.Success)
                                } else {
                                    trySend(
                                        RepositoryResponse.Error(
                                            message = updateTask.exception?.localizedMessage
                                                ?: "Unknown error while adding group id to user's groups array"
                                        )
                                    )
                                }
                            }
                    } else {
                        trySend(
                            RepositoryResponse.Error(
                                message = task.exception?.localizedMessage
                                    ?: "Unknown error while creating study group"
                            )
                        )
                    }
                }
            awaitClose()
        }

    override suspend fun listStudyGroups(): Flow<List<StudyGroup>> = callbackFlow {
        firestore.collection(STUDY_GROUPS_COLLECTION)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e(TAG, "Error fetching study groups", error)
                    return@addSnapshotListener
                }

                if (value != null) {
                    trySend(value.toObjects())
                } else {
                    trySend(emptyList())
                }
            }
        awaitClose()
    }

    override suspend fun myStudyGroups(currentUserId: String): Flow<List<StudyGroup>> =
        callbackFlow {
            firestore.collection(STUDY_GROUPS_COLLECTION)
                .whereArrayContains(StudyGroup::members.name, currentUserId)
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        Log.e(TAG, "Error fetching my created study groups", error)
                        return@addSnapshotListener
                    }

                    if (value != null) {
                        trySend(value.toObjects())
                    } else {
                        trySend(emptyList())
                    }
                }
            awaitClose()
        }

    override suspend fun getStudyGroup(groupId: String): Flow<StudyGroup?> = callbackFlow {
        firestore.collection(STUDY_GROUPS_COLLECTION)
            .document(groupId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null) {
                    trySend(documentSnapshot.toObject(StudyGroup::class.java))
                } else {
                    trySend(null)
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error fetching study group", exception)
                trySend(null)
            }
        awaitClose()
    }

    override suspend fun updateGroupDetails(group: StudyGroup): Flow<RepositoryResponse> =
        callbackFlow {
            val groupMap = hashMapOf<String, Any?>(
                StudyGroup::name.name to group.name,
                StudyGroup::courseCode.name to group.courseCode,
                StudyGroup::courseTitle.name to group.courseTitle,
                StudyGroup::courseDept.name to group.courseDept,
                StudyGroup::description.name to group.description,
                StudyGroup::locationName.name to group.locationName,
                StudyGroup::locationLink.name to group.locationLink,
                StudyGroup::meetingDate.name to group.meetingDate,
            )

            firestore.collection(STUDY_GROUPS_COLLECTION)
                .document(group.groupId)
                .update(groupMap)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        trySend(RepositoryResponse.Success)
                    } else {
                        trySend(
                            RepositoryResponse.Error(
                                message = task.exception?.localizedMessage
                                    ?: "Unknown error while updating study group"
                            )
                        )
                    }
                }
            awaitClose()
        }

    override suspend fun deleteStudyGroup(groupId: String): Flow<RepositoryResponse> =
        callbackFlow {
            firestore.collection(STUDY_GROUPS_COLLECTION)
                .document(groupId)
                .delete()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        trySend(RepositoryResponse.Success)
                    } else {
                        trySend(
                            RepositoryResponse.Error(
                                message = task.exception?.localizedMessage
                                    ?: "Unknown error while deleting study group"
                            )
                        )
                    }
                }
            awaitClose()
        }

    override suspend fun joinStudyGroup(
        studentId: String,
        groupId: String
    ): Flow<RepositoryResponse> = callbackFlow {
        firestore.collection(STUDY_GROUPS_COLLECTION)
            .document(groupId)
            .update(StudyGroup::members.name, FieldValue.arrayUnion(studentId))
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    firestore.collection(USERS_COLLECTION)
                        .document(studentId)
                        .update(User::myGroups.name, FieldValue.arrayUnion(groupId))
                        .addOnCompleteListener { updateTask ->
                            if (updateTask.isSuccessful) {
                                trySend(RepositoryResponse.Success)
                            } else {
                                trySend(
                                    RepositoryResponse.Error(
                                        message = updateTask.exception?.localizedMessage
                                            ?: "Unknown error while adding group id to user's groups array"
                                    )
                                )
                            }
                        }
                } else {
                    trySend(
                        RepositoryResponse.Error(
                            message = task.exception?.localizedMessage
                                ?: "Unknown error while joining group"
                        )
                    )
                }
            }
        awaitClose()
    }

    override suspend fun leaveStudyGroup(
        studentId: String,
        groupId: String
    ): Flow<RepositoryResponse> =
        callbackFlow {
            firestore.collection(STUDY_GROUPS_COLLECTION)
                .document(groupId)
                .update(StudyGroup::members.name, FieldValue.arrayRemove(studentId))
                .addOnCompleteListener { groupTask ->
                    if (groupTask.isSuccessful) {
                        firestore.collection(USERS_COLLECTION)
                            .document(studentId)
                            .update(User::myGroups.name, FieldValue.arrayRemove(groupId))
                            .addOnCompleteListener { userTask ->
                                if (userTask.isSuccessful) {
                                    trySend(RepositoryResponse.Success)
                                } else {
                                    trySend(
                                        RepositoryResponse.Error(
                                            message = userTask.exception?.localizedMessage
                                                ?: "Unknown error while leaving group"
                                        )
                                    )
                                }
                            }
                    } else {
                        trySend(
                            RepositoryResponse.Error(
                                message = groupTask.exception?.localizedMessage
                                    ?: "Unknown error while leaving group"
                            )
                        )
                    }
                }
            awaitClose()
        }

    override suspend fun sendMessage(
        groupId: String,
        message: ChatMessage
    ): Flow<RepositoryResponse> = callbackFlow {
        firestore.collection(MESSAGES_COLLECTION)
            .add(message)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    trySend(RepositoryResponse.Success)
                } else {
                    trySend(
                        RepositoryResponse.Error(
                            message = task.exception?.localizedMessage
                                ?: "Unknown error while sending message"
                        )
                    )
                }
            }
        awaitClose()
    }

    override suspend fun getChatMessages(groupId: String): Flow<List<ChatMessage>> =
        callbackFlow {
            firestore.collection(MESSAGES_COLLECTION)
                .whereEqualTo(ChatMessage::groupId.name, groupId)
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        Log.e(TAG, "Error fetching messages", error)
                        return@addSnapshotListener
                    }

                    if (value != null) {
                        trySend(value.toObjects())
                    } else {
                        trySend(emptyList())
                    }
                }
            awaitClose()
        }

    override suspend fun getLastMessageSent(groupId: String): Flow<ChatMessage?> = callbackFlow {
        firestore.collection(MESSAGES_COLLECTION)
            .whereEqualTo(ChatMessage::groupId.name, groupId)
            .orderBy(ChatMessage::timestamp.name, Query.Direction.DESCENDING)
            .limit(1)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e(TAG, "Error fetching last message", error)
                    return@addSnapshotListener
                }

                if (value != null) {
                    trySend(value.toObjects<ChatMessage>().firstOrNull())
                } else {
                    trySend(null)
                }
            }
        awaitClose()
    }

    override suspend fun getUserProfile(userId: String): Flow<User?> = callbackFlow {
        firestore.collection(USERS_COLLECTION)
            .document(userId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null) {
                    trySend(documentSnapshot.toObject(User::class.java))
                } else {
                    trySend(null)
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error fetching user profile", exception)
                trySend(null)
            }
        awaitClose()
    }

    override suspend fun createUserProfile(user: User): Flow<RepositoryResponse> = callbackFlow {
        firestore.collection(USERS_COLLECTION)
            .document(user.uid)
            .set(
                User(
                    name = user.name,
                    email = user.email,
                    photoUri = user.photoUri
                ),
                SetOptions.merge()
            )
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    trySend(RepositoryResponse.Success)
                } else {
                    trySend(
                        RepositoryResponse.Error(
                            message = task.exception?.localizedMessage
                                ?: "Unknown error while creating user profile"
                        )
                    )
                }
            }
        awaitClose()
    }

    override suspend fun updateUserProfile(user: User): Flow<RepositoryResponse> = callbackFlow {
        val userMap = hashMapOf<String, Any>(
            User::name.name to user.name,
            User::photoUri.name to user.photoUri.toString(),
            User::major.name to user.major,
            User::availabilityNotes.name to user.availabilityNotes,
        )

        firestore.collection(USERS_COLLECTION)
            .document(user.uid)
            .update(userMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    trySend(RepositoryResponse.Success)
                } else {
                    trySend(
                        RepositoryResponse.Error(
                            message = task.exception?.localizedMessage
                                ?: "Unknown error while updating user profile"
                        )
                    )
                }
            }
        awaitClose()
    }
}