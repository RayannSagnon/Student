package com.studentos.app.data.repository

import com.studentos.app.data.local.dao.SettingsDao
import com.studentos.app.data.local.entity.toDomain
import com.studentos.app.data.local.entity.toEntity
import com.studentos.app.domain.model.UserSettings
import com.studentos.app.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class SettingsRepositoryImpl(private val settingsDao: SettingsDao) : SettingsRepository {
    override val settings: Flow<UserSettings> = settingsDao.getSettings().map { 
        it?.toDomain() ?: UserSettings()
    }

    private suspend fun getCurrentSettings(): UserSettings {
        return settings.first()
    }

    override suspend fun updatePomodoroDuration(duration: Int) {
        settingsDao.insertSettings(getCurrentSettings().copy(pomodoroDuration = duration).toEntity())
    }

    override suspend fun updateShortBreakDuration(duration: Int) {
        settingsDao.insertSettings(getCurrentSettings().copy(shortBreakDuration = duration).toEntity())
    }

    override suspend fun updateLongBreakDuration(duration: Int) {
        settingsDao.insertSettings(getCurrentSettings().copy(longBreakDuration = duration).toEntity())
    }

    override suspend fun toggleDeadlineReminders(enabled: Boolean) {
        settingsDao.insertSettings(getCurrentSettings().copy(deadlineRemindersEnabled = enabled).toEntity())
    }

    override suspend fun toggleDailyStudyReminder(enabled: Boolean) {
        settingsDao.insertSettings(getCurrentSettings().copy(dailyStudyReminderEnabled = enabled).toEntity())
    }

    override suspend fun toggleStreakReminder(enabled: Boolean) {
        settingsDao.insertSettings(getCurrentSettings().copy(streakReminderEnabled = enabled).toEntity())
    }

    override suspend fun toggleDarkMode(enabled: Boolean) {
        settingsDao.insertSettings(getCurrentSettings().copy(isDarkMode = enabled).toEntity())
    }

    override suspend fun clearAllData() {
        // Implementation for clearing all data across all DAOs
    }
}
