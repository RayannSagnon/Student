package com.studentos.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.studentos.app.domain.repository.*

class ViewModelFactory(
    private val repository: Any,
    private val authRepository: AuthRepository? = null
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(repository as AuthRepository) as T
            }
            modelClass.isAssignableFrom(AssignmentViewModel::class.java) -> {
                AssignmentViewModel(repository as AssignmentRepository, authRepository!!) as T
            }
            modelClass.isAssignableFrom(CourseViewModel::class.java) -> {
                CourseViewModel(repository as CourseRepository, authRepository!!) as T
            }
            modelClass.isAssignableFrom(StudySessionViewModel::class.java) -> {
                StudySessionViewModel(repository as StudySessionRepository, authRepository!!) as T
            }
            modelClass.isAssignableFrom(DashboardViewModel::class.java) -> {
                val provider = repository as MultiRepositoryProvider
                DashboardViewModel(
                    provider.assignmentRepo, 
                    provider.courseRepo, 
                    provider.sessionRepo, 
                    authRepository!!
                ) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                val provider = repository as MultiRepositoryProvider
                ProfileViewModel(
                    provider.courseRepo, 
                    provider.assignmentRepo, 
                    provider.sessionRepo, 
                    authRepository!!
                ) as T
            }
            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> {
                SettingsViewModel(repository as SettingsRepository) as T
            }
            modelClass.isAssignableFrom(AcademicAnalyticsViewModel::class.java) -> {
                val provider = repository as MultiRepositoryProvider
                AcademicAnalyticsViewModel(
                    provider.courseRepo,
                    provider.assignmentRepo,
                    provider.sessionRepo,
                    authRepository!!
                ) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

data class MultiRepositoryProvider(
    val courseRepo: CourseRepository,
    val assignmentRepo: AssignmentRepository,
    val sessionRepo: StudySessionRepository
)
