package com.studentos.app.domain.repository

import com.studentos.app.domain.model.UserSettings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val settings: Flow<UserSettings>
    suspend fun updatePomodoroDuration(duration: Int)
    suspend fun updateShortBreakDuration(duration: Int)
    suspend fun updateLongBreakDuration(duration: Int)
    suspend fun toggleDeadlineReminders(enabled: Boolean)
    suspend fun toggleDailyStudyReminder(enabled: Boolean)
    suspend fun toggleStreakReminder(enabled: Boolean)
    suspend fun toggleDarkMode(enabled: Boolean)
    suspend fun clearAllData()
}
