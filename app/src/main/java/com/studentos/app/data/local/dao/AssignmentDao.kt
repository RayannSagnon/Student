package com.studentos.app.data.local.dao

import androidx.room.*
import com.studentos.app.data.local.entity.AssignmentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AssignmentDao {
    @Query("SELECT * FROM assignments WHERE userId = :userId")
    fun getAssignmentsForUser(userId: String): Flow<List<AssignmentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssignment(assignment: AssignmentEntity)

    @Update
    suspend fun updateAssignment(assignment: AssignmentEntity)

    @Query("UPDATE assignments SET completed = :completed WHERE id = :id")
    suspend fun toggleAssignmentCompletion(id: String, completed: Boolean)

    @Delete
    suspend fun deleteAssignment(assignment: AssignmentEntity)
}
