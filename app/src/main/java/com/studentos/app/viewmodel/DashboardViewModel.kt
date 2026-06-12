package com.studentos.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studentos.app.domain.model.Assignment
import com.studentos.app.domain.model.Course
import com.studentos.app.domain.repository.AssignmentRepository
import com.studentos.app.domain.repository.CourseRepository
import com.studentos.app.domain.repository.AuthRepository
import com.studentos.app.domain.repository.StudySessionRepository
import com.studentos.app.domain.planner.DailyPlanner
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

data class DashboardUiState(
    val upcomingAssignments: List<Assignment> = emptyList(),
    val activeTaskCount: Int = 0,
    val nextCourse: Course? = null,
    val recommendedTasks: List<Assignment> = emptyList(),
    val recommendedFocusCourse: Course? = null,
    val weeklyProgress: Float = 0.0f,
    val studyStreak: Int = 0,
    val isLoading: Boolean = true
)

class DashboardViewModel(
    private val assignmentRepository: AssignmentRepository,
    private val courseRepository: CourseRepository,
    private val sessionRepository: StudySessionRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val planner = DailyPlanner()

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<DashboardUiState> = authRepository.currentUser
        .flatMapLatest { user ->
            if (user != null) {
                combine(
                    assignmentRepository.getAssignments(user.uid),
                    courseRepository.getCourses(user.uid),
                    flow { emit(sessionRepository.calculateStreak(user.uid)) }
                ) { assignments, courses, streak ->
                    val activeAssignments = assignments.filter { !it.completed }
                        .sortedBy { it.dueDate }
                    
                    val plan = planner.generatePlan(assignments, courses)
                    
                    val completedThisWeek = assignments.count { it.completed }
                    val progress = if (assignments.isNotEmpty()) completedThisWeek.toFloat() / assignments.size else 0f
                    
                    DashboardUiState(
                        upcomingAssignments = activeAssignments.take(3),
                        activeTaskCount = activeAssignments.size,
                        nextCourse = courses.firstOrNull(),
                        recommendedTasks = plan.recommendedTasks,
                        recommendedFocusCourse = plan.recommendedStudySessions.firstOrNull(),
                        weeklyProgress = progress,
                        studyStreak = streak,
                        isLoading = false
                    )
                }
            } else {
                flowOf(DashboardUiState(isLoading = false))
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DashboardUiState())
}
