package com.studentos.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.studentos.app.domain.model.Course
import com.studentos.app.domain.model.CourseDifficulty

@Entity(tableName = "courses")
data class CourseEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val name: String,
    val professor: String,
    val color: String,
    val difficulty: String, // Room enum handling: EASY, MEDIUM, HARD
    val courseCode: String?,
    val targetGrade: String?,
    val updatedAt: Long,
    val syncState: String = "PENDING"
)

fun CourseEntity.toDomain() = Course(
    id = id,
    userId = userId,
    name = name,
    professor = professor,
    color = color,
    difficulty = CourseDifficulty.valueOf(difficulty),
    courseCode = courseCode,
    targetGrade = targetGrade,
    updatedAt = updatedAt
)

fun Course.toEntity() = CourseEntity(
    id = id,
    userId = userId,
    name = name,
    professor = professor,
    color = color,
    difficulty = difficulty.name,
    courseCode = courseCode,
    targetGrade = targetGrade,
    updatedAt = updatedAt
)
