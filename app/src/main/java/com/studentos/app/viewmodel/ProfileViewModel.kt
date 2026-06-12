package com.studentos.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studentos.app.domain.model.Assignment
import com.studentos.app.domain.model.Course
import com.studentos.app.domain.model.StudySession
import com.studentos.app.domain.repository.AssignmentRepository
import com.studentos.app.domain.repository.AuthRepository
import com.studentos.app.domain.repository.CourseRepository
import com.studentos.app.domain.repository.StudySessionRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import java.time.Duration
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

data class ProfileUiState(
    val courseCount: Int = 0,
    val totalTasks: Int = 0,
    val completedTasks: Int = 0,
    val focusHours: Double = 0.0,
    val sessionCount: Int = 0,
    val weeklyFocusHours: Double = 0.0,
    val isLoading: Boolean = true
)

class ProfileViewModel(
    private val courseRepository: CourseRepository,
    private val assignmentRepository: AssignmentRepository,
    private val sessionRepository: StudySessionRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<ProfileUiState> = authRepository.currentUser
        .flatMapLatest { user ->
            if (user != null) {
                combine(
                    courseRepository.getCourses(user.uid),
                    assignmentRepository.getAssignments(user.uid),
                    sessionRepository.getSessions(user.uid)
                ) { courses, assignments, sessions ->
                    val now = LocalDateTime.now()
                    val startOfWeek = now.minusDays(now.dayOfWeek.value.toLong() - 1).truncatedTo(ChronoUnit.DAYS)
                    
                    val weeklySessions = sessions.filter { it.startTime.isAfter(startOfWeek) }
                    
                    ProfileUiState(
                        courseCount = courses.size,
                        totalTasks = assignments.size,
                        completedTasks = assignments.count { it.completed },
                        focusHours = sessions.sumOf { 
                            Duration.between(it.startTime, it.endTime).toMinutes().toDouble()
                        } / 60.0,
                        sessionCount = sessions.size,
                        weeklyFocusHours = weeklySessions.sumOf {
                            Duration.between(it.startTime, it.endTime).toMinutes().toDouble()
                        } / 60.0,
                        isLoading = false
                    )
                }
            } else {
                flowOf(ProfileUiState(isLoading = false))
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProfileUiState())
}
