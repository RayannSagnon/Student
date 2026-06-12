package com.studentos.app.data.repository

import com.studentos.app.data.local.dao.StudySessionDao
import com.studentos.app.data.local.entity.toDomain
import com.studentos.app.data.local.entity.toEntity
import com.studentos.app.domain.model.StudySession
import com.studentos.app.domain.repository.StudySessionRepository
import kotlinx.coroutines.flow.Flow



















































import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class StudySessionRepositoryImpl(private val studySessionDao: StudySessionDao) : StudySessionRepository {
    override fun getSessions(userId: String): Flow<List<StudySession>> = 
        studySessionDao.getSessionsForUser(userId).map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun addSession(session: StudySession) {
        studySessionDao.insertSession(session.toEntity())
    }

    override suspend fun calculateStreak(userId: String): Int {
        val sessions = studySessionDao.getSessionsForUser(userId).first()
            .map { it.toDomain() }
            .sortedByDescending { it.startTime }
        
        if (sessions.isEmpty()) return 0
        
        val distinctDates = sessions.map { it.startTime.toLocalDate() }.distinct()
        val today = LocalDate.now()
        
        if (distinctDates.first().isBefore(today.minusDays(1))) return 0
        
        var streak = 0
        var currentDate = distinctDates.first()
        
        for (date in distinctDates) {
            if (date == currentDate) {
                streak++
                currentDate = currentDate.minusDays(1)
            } else {
                break
            }
        }
        return streak
    }
}
