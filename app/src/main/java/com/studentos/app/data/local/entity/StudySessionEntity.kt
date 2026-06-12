package com.studentos.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.studentos.app.domain.model.StudySession
import java.time.LocalDateTime

@Entity(tableName = "study_sessions")
data class StudySessionEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val courseId: String,
    val startTime: String,
    val endTime: String,
    val completed: Boolean,
    val updatedAt: Long,
    val syncState: String = "PENDING"
)

fun StudySessionEntity.toDomain() = StudySession(
    id = id,
    userId = userId,
    courseId = courseId,
    startTime = LocalDateTime.parse(startTime),
    endTime = LocalDateTime.parse(endTime),
    completed = completed,
    updatedAt = updatedAt
)

fun StudySession.toEntity() = StudySessionEntity(
    id = id,
    userId = userId,
    courseId = courseId,
    startTime = startTime.toString(),
    endTime = endTime.toString(),
    completed = completed,
    updatedAt = updatedAt
)
