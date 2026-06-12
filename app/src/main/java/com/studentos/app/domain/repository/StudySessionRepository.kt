package com.studentos.app.domain.repository

import com.studentos.app.domain.model.StudySession
import kotlinx.coroutines.flow.Flow

interface StudySessionRepository {
    fun getSessions(userId: String): Flow<List<StudySession>>
    suspend fun addSession(session: StudySession)
    suspend fun calculateStreak(userId: String): Int
}
