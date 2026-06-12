package com.studentos.app.domain.repository

import com.studentos.app.domain.model.Course
import kotlinx.coroutines.flow.Flow

interface CourseRepository {
    fun getCourses(userId: String): Flow<List<Course>>
    suspend fun getCourseById(id: String): Course?
    suspend fun addCourse(course: Course)
}
