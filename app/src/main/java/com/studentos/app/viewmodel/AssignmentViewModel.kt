package com.studentos.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studentos.app.domain.model.Assignment
import com.studentos.app.domain.model.AssignmentPriority
import com.studentos.app.domain.repository.AssignmentRepository
import com.studentos.app.domain.repository.AuthRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.UUID

class AssignmentViewModel(
    private val repository: AssignmentRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val assignments: StateFlow<List<Assignment>> = authRepository.currentUser
        .flatMapLatest { user ->
            if (user != null) {
                repository.getAssignments(user.uid)
            } else {
                flowOf(emptyList())
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addAssignment(
        title: String, 
        courseId: String, 
        dueDate: LocalDateTime,
        priority: AssignmentPriority = AssignmentPriority.MEDIUM,
        estimatedEffortHours: Int = 1
    ) {
        val userId = authRepository.getUserId() ?: return
        viewModelScope.launch {
            val newAssignment = Assignment(
                id = UUID.randomUUID().toString(),
                userId = userId,
                title = title,
                courseId = courseId,
                dueDate = dueDate,
                completed = false,
                priority = priority,
                estimatedEffortHours = estimatedEffortHours
            )
            repository.addAssignment(newAssignment)
        }
    }

    fun toggleAssignment(assignment: Assignment) {
        viewModelScope.launch {
            repository.toggleAssignmentCompletion(assignment.id, !assignment.completed)
        }
    }
}
