package com.studentos.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studentos.app.domain.model.Course
import com.studentos.app.domain.model.CourseDifficulty
import com.studentos.app.domain.repository.CourseRepository
import com.studentos.app.domain.repository.AuthRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class CourseViewModel(
    private val repository: CourseRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val courses: StateFlow<List<Course>> = authRepository.currentUser
        .flatMapLatest { user ->
            if (user != null) {
                repository.getCourses(user.uid)
            } else {
                flowOf(emptyList())
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    suspend fun getCourseById(id: String): Course? {
        return repository.getCourseById(id)
    }

    fun addCourse(
        name: String, 
        professor: String, 
        color: String, 
        difficulty: CourseDifficulty, 
        courseCode: String?, 
        targetGrade: String?
    ) {
        val userId = authRepository.getUserId() ?: return
        viewModelScope.launch {
            val newCourse = Course(
                id = UUID.randomUUID().toString(),
                userId = userId,
                name = name,
                professor = professor,
                color = color,
                difficulty = difficulty,
                courseCode = courseCode,
                targetGrade = targetGrade
            )
            repository.addCourse(newCourse)
        }
    }
}
