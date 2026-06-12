package com.studentos.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studentos.app.domain.model.UserSettings
import com.studentos.app.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(private val repository: SettingsRepository) : ViewModel() {

    val settings: StateFlow<UserSettings> = repository.settings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserSettings())

    fun updatePomodoroDuration(duration: Int) {
        viewModelScope.launch { repository.updatePomodoroDuration(duration) }
    }

    fun toggleDeadlineReminders(enabled: Boolean) {
        viewModelScope.launch { repository.toggleDeadlineReminders(enabled) }
    }

    fun toggleDailyStudyReminder(enabled: Boolean) {
        viewModelScope.launch { repository.toggleDailyStudyReminder(enabled) }
    }

    fun toggleStreakReminder(enabled: Boolean) {
        viewModelScope.launch { repository.toggleStreakReminder(enabled) }
    }

    fun toggleDarkMode(enabled: Boolean) {
        viewModelScope.launch { repository.toggleDarkMode(enabled) }
    }
}
