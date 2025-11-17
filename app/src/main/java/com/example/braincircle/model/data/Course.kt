package com.example.braincircle.model.data

import com.google.firebase.firestore.DocumentId

/**
 * Model for a course in the university catalog.
 *
 * @param courseId The unique identifier for the course.
 * @param code The course code, e.g., "CS101".
 * @param title The course title, e.g., "Introduction to Computer Science".
 * @param department The department that offers the course, e.g., "Computer Science".
 */
data class Course(
    @DocumentId val courseId: String = "",
    val code: String = "",
    val title: String = "",
    val department: String = ""
)
