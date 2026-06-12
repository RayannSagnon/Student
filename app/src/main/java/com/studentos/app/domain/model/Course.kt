package com.studentos.app.domain.model

enum class CourseDifficulty {
    EASY, MEDIUM, HARD
}

data class Course(
    val id: String,
    val userId: String,
    val name: String,
    val professor: String,
    val color: String,
    val difficulty: CourseDifficulty = CourseDifficulty.MEDIUM,
    val courseCode: String? = null,
    val targetGrade: String? = null,
    val updatedAt: Long = System.currentTimeMillis()
)
