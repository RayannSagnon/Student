package com.studentos.app.data

import android.content.Context
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.studentos.app.data.local.database.AppDatabase
import com.studentos.app.data.remote.auth.FirebaseAuthRepository
import com.studentos.app.data.remote.auth.MockAuthRepository
import com.studentos.app.data.remote.firestore.FirestoreSyncManager
import com.studentos.app.data.repository.AssignmentRepositoryImpl
import com.studentos.app.data.repository.CourseRepositoryImpl
import com.studentos.app.data.repository.SettingsRepositoryImpl
import com.studentos.app.data.repository.StudySessionRepositoryImpl
import com.studentos.app.domain.repository.AssignmentRepository
import com.studentos.app.domain.repository.AuthRepository
import com.studentos.app.domain.repository.CourseRepository
import com.studentos.app.domain.repository.SettingsRepository
import com.studentos.app.domain.repository.StudySessionRepository

object Graph {
    lateinit var database: AppDatabase
        private set

    lateinit var authRepository: AuthRepository
        private set

    lateinit var courseRepository: CourseRepository
        private set

    lateinit var assignmentRepository: AssignmentRepository
        private set

    lateinit var studySessionRepository: StudySessionRepository
        private set

    lateinit var settingsRepository: SettingsRepository
        private set

    var syncManager: FirestoreSyncManager? = null
        private set

    fun provide(context: Context) {
        database = AppDatabase.getDatabase(context)
        
        try {
            // Try to initialize Firebase
            FirebaseApp.initializeApp(context)
            val firebaseAuth = FirebaseAuth.getInstance()
            val firestore = FirebaseFirestore.getInstance()
            authRepository = FirebaseAuthRepository(firebaseAuth)
            
            syncManager = FirestoreSyncManager(
                firestore,
                database.courseDao(),
                database.assignmentDao(),
                database.studySessionDao()
            )
            Log.d("Graph", "Firebase initialized successfully")
        } catch (e: Exception) {
            Log.e("Graph", "Firebase init failed, using MockAuthRepository: ${e.message}")
            authRepository = MockAuthRepository()
        }
        
        courseRepository = CourseRepositoryImpl(database.courseDao())
        assignmentRepository = AssignmentRepositoryImpl(database.assignmentDao())
        studySessionRepository = StudySessionRepositoryImpl(database.studySessionDao())
        settingsRepository = SettingsRepositoryImpl(database.settingsDao())
    }
}
