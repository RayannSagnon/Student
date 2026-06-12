package com.studentos.app.domain.model

data class UserSettings(
    val pomodoroDuration: Int = 25,
    val shortBreakDuration: Int = 5,
    val longBreakDuration: Int = 15,
    val deadlineRemindersEnabled: Boolean = true,
    val dailyStudyReminderEnabled: Boolean = true,
    val streakReminderEnabled: Boolean = true,
    val isDarkMode: Boolean = false
)
