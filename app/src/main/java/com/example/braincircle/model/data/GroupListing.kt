package com.example.braincircle.model.data

import com.google.firebase.firestore.DocumentId

/**
 * Represents the search page for a specific course.
 * It contains a list of students who are interested in the course.
 *
 * @param courseId The unique identifier of the course, e.g., "CS101".
 * @param interestedStudents A list of students who are interested in the course.
 */
data class GroupListing(
    @DocumentId val courseId: String = "",
    val interestedStudents: List<StudentInterest> = emptyList()
)

/**
 * Sub-model stored in [GroupListing]
 * It contains the details of a student who is interested in a specific course.
 *
 * @param uid The unique identifier of the student.
 * @param name The name of the student.
 * @param availability The availability of the student.
 */
data class StudentInterest(
    val uid: String = "",
    val name: String = "",
    val availability: String = ""
)
