package com.studentos.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.studentos.app.domain.model.Assignment
import com.studentos.app.domain.model.AssignmentPriority
import java.time.LocalDateTime

@Entity(tableName = "assignments")
data class AssignmentEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val title: String,
    val courseId: String,
    val dueDate: String,
    val completed: Boolean,
    val priority: String, // Room enum handling: LOW, MEDIUM, HIGH
    val estimatedEffortHours: Int,
    val updatedAt: Long,
    val syncState: String = "PENDING"
)

fun AssignmentEntity.toDomain() = Assignment(
    id = id,
    userId = userId,
    title = title,
    courseId = courseId,
    dueDate = LocalDateTime.parse(dueDate),
    completed = completed,
    priority = AssignmentPriority.valueOf(priority),
    estimatedEffortHours = estimatedEffortHours,
    updatedAt = updatedAt
)

fun Assignment.toEntity() = AssignmentEntity(
    id = id,
    userId = userId,
    title = title,
    courseId = courseId,
    dueDate = dueDate.toString(),
    completed = completed,
    priority = priority.name,
    estimatedEffortHours = estimatedEffortHours,
    updatedAt = updatedAt
)
