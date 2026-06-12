package com.studentos.app.data.repository

import com.studentos.app.data.local.dao.AssignmentDao
import com.studentos.app.data.local.entity.toDomain
import com.studentos.app.data.local.entity.toEntity
import com.studentos.app.domain.model.Assignment
import com.studentos.app.domain.repository.AssignmentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AssignmentRepositoryImpl(private val assignmentDao: AssignmentDao) : AssignmentRepository {
    override fun getAssignments(userId: String): Flow<List<Assignment>> = assignmentDao.getAssignmentsForUser(userId).map { entities ->
        entities.map { it.toDomain() }
    }

    override suspend fun addAssignment(assignment: Assignment) {
        assignmentDao.insertAssignment(assignment.toEntity())
    }

    override suspend fun toggleAssignmentCompletion(id: String, completed: Boolean) {
        assignmentDao.toggleAssignmentCompletion(id, completed)
    }
}
