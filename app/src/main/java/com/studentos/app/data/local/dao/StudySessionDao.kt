package com.studentos.app.data.local.dao

import androidx.room.*
import com.studentos.app.data.local.entity.StudySessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StudySessionDao {
    @Query("SELECT * FROM study_sessions WHERE userId = :userId")
    fun getSessionsForUser(userId: String): Flow<List<StudySessionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: StudySessionEntity)

    @Delete
    suspend fun deleteSession(session: StudySessionEntity)
}
