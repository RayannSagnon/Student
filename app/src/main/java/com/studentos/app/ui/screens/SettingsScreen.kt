package com.studentos.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.studentos.app.data.Graph
import com.studentos.app.viewmodel.SettingsViewModel
import com.studentos.app.viewmodel.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = viewModel(
        factory = ViewModelFactory(Graph.settingsRepository)
    )
) {
    val settings by viewModel.settings.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            SettingsSection(title = "Study Timer") {
                SliderSetting(
                    label = "Pomodoro Duration",
                    value = settings.pomodoroDuration.toFloat(),
                    valueRange = 10f..60f,
                    steps = 10,
                    onValueChange = { viewModel.updatePomodoroDuration(it.toInt()) },
                    unit = "min"
                )
            }

            SettingsSection(title = "Notifications") {
                SwitchSetting(
                    icon = Icons.Outlined.NotificationsActive,
                    label = "Deadline Reminders",
                    description = "Get notified before assignments are due",
                    checked = settings.deadlineRemindersEnabled,
                    onCheckedChange = { viewModel.toggleDeadlineReminders(it) }
                )
                SwitchSetting(
                    icon = Icons.Outlined.EventRepeat,
                    label = "Daily Study Reminder",
                    description = "Gentle nudge to start your study block",
                    checked = settings.dailyStudyReminderEnabled,
                    onCheckedChange = { viewModel.toggleDailyStudyReminder(it) }
                )
                SwitchSetting(
                    icon = Icons.Outlined.LocalFireDepartment,
                    label = "Streak Notifications",
                    description = "Alerts when your streak is at risk",
                    checked = settings.streakReminderEnabled,
                    onCheckedChange = { viewModel.toggleStreakReminder(it) }
                )
            }

            SettingsSection(title = "Appearance") {
                SwitchSetting(
                    icon = Icons.Outlined.DarkMode,
                    label = "Dark Mode",
                    description = "Force dark theme",
                    checked = settings.isDarkMode,
                    onCheckedChange = { viewModel.toggleDarkMode(it) }
                )
            }

            SettingsSection(title = "Danger Zone") {
                Button(
                    onClick = { /* Handle reset */ },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("Reset All Local Data")
                }
            }
        }
    }
}

@Composable
fun SettingsSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Card(
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                content()
            }
        }
    }
}

@Composable
fun SwitchSetting(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(label, style = MaterialTheme.typography.bodyLarge)
            Text(description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
fun SliderSetting(
    label: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int,
    onValueChange: (Float) -> Unit,
    unit: String
) {
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, style = MaterialTheme.typography.bodyLarge)
            Text("${value.toInt()} $unit", style = MaterialTheme.typography.bodyLarge, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            steps = steps
        )
    }
}
