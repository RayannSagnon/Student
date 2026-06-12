package com.studentos.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studentos.app.domain.model.StudySession
import com.studentos.app.domain.repository.StudySessionRepository
import com.studentos.app.domain.repository.AuthRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.UUID

class StudySessionViewModel(
    private val repository: StudySessionRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val sessions: StateFlow<List<StudySession>> = authRepository.currentUser
        .flatMapLatest { user ->
            if (user != null) {
                repository.getSessions(user.uid)
            } else {
                flowOf(emptyList())
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun completeSession(courseId: String, startTime: LocalDateTime, endTime: LocalDateTime) {
        val userId = authRepository.getUserId() ?: return
        viewModelScope.launch {
            val newSession = StudySession(
                id = UUID.randomUUID().toString(),
                userId = userId,
                courseId = courseId,
                startTime = startTime,
                endTime = endTime
            )
            repository.addSession(newSession)
        }
    }
}
