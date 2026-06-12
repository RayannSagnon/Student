package com.studentos.app.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.studentos.app.data.local.dao.AssignmentDao
import com.studentos.app.data.local.dao.CourseDao
import com.studentos.app.data.local.dao.SettingsDao
import com.studentos.app.data.local.dao.StudySessionDao
import com.studentos.app.data.local.entity.AssignmentEntity
import com.studentos.app.data.local.entity.CourseEntity
import com.studentos.app.data.local.entity.SettingsEntity
import com.studentos.app.data.local.entity.StudySessionEntity

@Database(
    entities = [CourseEntity::class, AssignmentEntity::class, StudySessionEntity::class, SettingsEntity::class],
    version = 4, // Incremented version for Settings
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun courseDao(): CourseDao
    abstract fun assignmentDao(): AssignmentDao
    abstract fun studySessionDao(): StudySessionDao
    abstract fun settingsDao(): SettingsDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "studentos_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                .also { Instance = it }
            }
        }
    }
}
