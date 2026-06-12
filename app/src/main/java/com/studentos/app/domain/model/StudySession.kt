package com.studentos.app.domain.model

import java.time.LocalDateTime

data class StudySession(
    val id: String,
    val userId: String,
    val courseId: String,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val completed: Boolean = true,
    val updatedAt: Long = System.currentTimeMillis()
)
