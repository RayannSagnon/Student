package com.studentos.app.data.repository

import com.studentos.app.data.local.dao.CourseDao
import com.studentos.app.data.local.entity.toDomain
import com.studentos.app.data.local.entity.toEntity
import com.studentos.app.domain.model.Course
import com.studentos.app.domain.repository.CourseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CourseRepositoryImpl(private val courseDao: CourseDao) : CourseRepository {
    override fun getCourses(userId: String): Flow<List<Course>> = courseDao.getCoursesForUser(userId).map { entities ->
        entities.map { it.toDomain() }
    }

    override suspend fun getCourseById(id: String): Course? {
        return courseDao.getCourseById(id)?.toDomain()
    }

    override suspend fun addCourse(course: Course) {
        courseDao.insertCourse(course.toEntity())
    }
}
