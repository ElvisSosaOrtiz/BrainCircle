package com.example.braincircle.model.data

import android.net.Uri
import com.google.firebase.firestore.DocumentId

/**
 * Model for a student in collection "users".
 *
 * @param uid Firebase Authentication ID as document ID.
 * @param name The name of the student.
 * @param email The email of the student.
 * @param major Career or faculty of the student.
 * @param availabilityNotes Description of the student's availability.
 * @param myGroups List of groups IDs the student is a member of.
 */
data class User(
    @DocumentId val uid: String = "",
    val name: String = "",
    val email: String = "",
    val photoUri: Uri? = Uri.EMPTY,
    val major: String = "",
    val availabilityNotes: String = "",
    val myGroups: List<String> = emptyList()
)
