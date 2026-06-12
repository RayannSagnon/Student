package com.studentos.app.data.remote.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.studentos.app.data.local.dao.AssignmentDao
import com.studentos.app.data.local.dao.CourseDao
import com.studentos.app.data.local.dao.StudySessionDao
import com.studentos.app.data.local.entity.toDomain
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await

class FirestoreSyncManager(
    private val firestore: FirebaseFirestore,
    private val courseDao: CourseDao,
    private val assignmentDao: AssignmentDao,
    private val studySessionDao: StudySessionDao
) {
    suspend fun sync(userId: String) {
        syncCourses(userId)
        syncAssignments(userId)
        syncStudySessions(userId)
    }

    private suspend fun syncCourses(userId: String) {
        val localCourses = courseDao.getCoursesForUser(userId).first()
        
        for (local in localCourses) {
            firestore.collection("users").document(userId)
                .collection("courses").document(local.id)
                .set(local.toDomain()).await()
        }
    }

    private suspend fun syncAssignments(userId: String) {
        val localAssignments = assignmentDao.getAssignmentsForUser(userId).first()
        
        for (local in localAssignments) {
            firestore.collection("users").document(userId)
                .collection("assignments").document(local.id)
                .set(local.toDomain()).await()
        }
    }

    private suspend fun syncStudySessions(userId: String) {
        val localSessions = studySessionDao.getSessionsForUser(userId).first()
        
        for (local in localSessions) {
            firestore.collection("users").document(userId)
                .collection("studySessions").document(local.id)
                .set(local.toDomain()).await()
        }
    }
}
