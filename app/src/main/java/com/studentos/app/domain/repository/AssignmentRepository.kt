package com.studentos.app.domain.repository

import com.studentos.app.domain.model.Assignment
import kotlinx.coroutines.flow.Flow

interface AssignmentRepository {
    fun getAssignments(userId: String): Flow<List<Assignment>>
    suspend fun addAssignment(assignment: Assignment)
    suspend fun toggleAssignmentCompletion(id: String, completed: Boolean)
}
