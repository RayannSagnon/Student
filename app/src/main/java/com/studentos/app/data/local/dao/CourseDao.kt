package com.studentos.app.data.local.dao

import androidx.room.*
import com.studentos.app.data.local.entity.CourseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CourseDao {
    @Query("SELECT * FROM courses WHERE userId = :userId")
    fun getCoursesForUser(userId: String): Flow<List<CourseEntity>>

    @Query("SELECT * FROM courses WHERE id = :id")
    suspend fun getCourseById(id: String): CourseEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourse(course: CourseEntity)

    @Delete
    suspend fun deleteCourse(course: CourseEntity)
}
