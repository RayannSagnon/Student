package com.studentos.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studentos.app.domain.model.CourseDifficulty
import com.studentos.app.domain.repository.AssignmentRepository
import com.studentos.app.domain.repository.AuthRepository
import com.studentos.app.domain.repository.CourseRepository
import com.studentos.app.domain.repository.StudySessionRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import java.time.Duration
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

data class AnalyticsUiState(
    val totalCourses: Int = 0,
    val activeAssignments: Int = 0,
    val completedAssignments: Int = 0,
    val totalFocusHours: Double = 0.0,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val mostStudiedCourseName: String = "N/A",
    val upcomingDeadlineCount: Int = 0,
    val averageSessionDurationMinutes: Long = 0,
    val isLoading: Boolean = true
)

class AcademicAnalyticsViewModel(
    private val courseRepository: CourseRepository,
    private val assignmentRepository: AssignmentRepository,
    private val sessionRepository: StudySessionRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<AnalyticsUiState> = authRepository.currentUser
        .flatMapLatest { user ->
            if (user != null) {
                combine(
                    courseRepository.getCourses(user.uid),
                    assignmentRepository.getAssignments(user.uid),
                    sessionRepository.getSessions(user.uid),
                    flow { emit(sessionRepository.calculateStreak(user.uid)) }
                ) { courses, assignments, sessions, streak ->
                    
                    val totalMinutes = sessions.sumOf { Duration.between(it.startTime, it.endTime).toMinutes() }
                    
                    val courseStudyTime = sessions.groupBy { it.courseId }
                        .mapValues { entry -> 
                            entry.value.sumOf { Duration.between(it.startTime, it.endTime).toMinutes() }
                        }
                    
                    val mostStudiedCourseId = courseStudyTime.maxByOrNull { it.value }?.key
                    val mostStudiedCourseName = courses.find { it.id == mostStudiedCourseId }?.name ?: "None"

                    AnalyticsUiState(
                        totalCourses = courses.size,
                        activeAssignments = assignments.count { !it.completed },
                        completedAssignments = assignments.count { it.completed },
                        totalFocusHours = totalMinutes / 60.0,
                        currentStreak = streak,
                        longestStreak = streak, // Placeholder for actual longest streak logic
                        mostStudiedCourseName = mostStudiedCourseName,
                        upcomingDeadlineCount = assignments.count { !it.completed && it.dueDate.isAfter(LocalDateTime.now()) },
                        averageSessionDurationMinutes = if (sessions.isNotEmpty()) totalMinutes / sessions.size else 0,
                        isLoading = false
                    )
                }
            } else {
                flowOf(AnalyticsUiState(isLoading = false))
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AnalyticsUiState())
}
