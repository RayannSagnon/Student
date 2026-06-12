package com.studentos.app.domain.model

import java.time.LocalDateTime

enum class AssignmentPriority {
    LOW, MEDIUM, HIGH
}

data class Assignment(
    val id: String,
    val userId: String,
    val title: String,
    val courseId: String,
    val dueDate: LocalDateTime,
    val completed: Boolean,
    val priority: AssignmentPriority = AssignmentPriority.MEDIUM,
    val estimatedEffortHours: Int = 1,
    val updatedAt: Long = System.currentTimeMillis()
)
