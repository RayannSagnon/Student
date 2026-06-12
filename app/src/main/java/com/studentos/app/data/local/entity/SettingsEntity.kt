package com.studentos.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.studentos.app.domain.model.UserSettings

@Entity(tableName = "settings")
data class SettingsEntity(
    @PrimaryKey val id: Int = 0, // Single row for settings
    val pomodoroDuration: Int,
    val shortBreakDuration: Int,
    val longBreakDuration: Int,
    val deadlineRemindersEnabled: Boolean,
    val dailyStudyReminderEnabled: Boolean,
    val streakReminderEnabled: Boolean,
    val isDarkMode: Boolean
)

fun SettingsEntity.toDomain() = UserSettings(
    pomodoroDuration = pomodoroDuration,
    shortBreakDuration = shortBreakDuration,
    longBreakDuration = longBreakDuration,
    deadlineRemindersEnabled = deadlineRemindersEnabled,
    dailyStudyReminderEnabled = dailyStudyReminderEnabled,
    streakReminderEnabled = streakReminderEnabled,
    isDarkMode = isDarkMode
)

fun UserSettings.toEntity() = SettingsEntity(
    pomodoroDuration = pomodoroDuration,
    shortBreakDuration = shortBreakDuration,
    longBreakDuration = longBreakDuration,
    deadlineRemindersEnabled = deadlineRemindersEnabled,
    dailyStudyReminderEnabled = dailyStudyReminderEnabled,
    streakReminderEnabled = streakReminderEnabled,
    isDarkMode = isDarkMode
)
