package com.studentos.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.studentos.app.data.Graph
import com.studentos.app.viewmodel.AcademicAnalyticsViewModel
import com.studentos.app.viewmodel.MultiRepositoryProvider
import com.studentos.app.viewmodel.ViewModelFactory
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AcademicAnalyticsScreen(
    onBack: () -> Unit,
    viewModel: AcademicAnalyticsViewModel = viewModel(
        factory = ViewModelFactory(
            MultiRepositoryProvider(
                Graph.courseRepository,
                Graph.assignmentRepository,
                Graph.studySessionRepository
            ),
            Graph.authRepository
        )
    )
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Academic Analytics") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = "Overall Performance",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    AnalyticsCard(
                        modifier = Modifier.weight(1f),
                        title = "Current Streak",
                        value = "${uiState.currentStreak} Days",
                        icon = Icons.Default.LocalFireDepartment,
                        color = Color(0xFFFF5722)
                    )
                    AnalyticsCard(
                        modifier = Modifier.weight(1f),
                        title = "Focus Hours",
                        value = String.format(Locale.getDefault(), "%.1f", uiState.totalFocusHours),
                        icon = Icons.Default.Timer,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Text(
                    text = "Tasks & Courses",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                ) {
                    Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        StatRow(label = "Total Courses", value = uiState.totalCourses.toString())
                        StatRow(label = "Active Assignments", value = uiState.activeAssignments.toString())
                        StatRow(label = "Completed Tasks", value = uiState.completedAssignments.toString())
                        StatRow(label = "Upcoming Deadlines", value = uiState.upcomingDeadlineCount.toString())
                    }
                }

                Text(
                    text = "Study Habits",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                AnalyticsCard(
                    modifier = Modifier.fillMaxWidth(),
                    title = "Most Studied Course",
                    value = uiState.mostStudiedCourseName,
                    icon = Icons.Default.School,
                    color = MaterialTheme.colorScheme.secondary
                )

                AnalyticsCard(
                    modifier = Modifier.fillMaxWidth(),
                    title = "Avg. Session Length",
                    value = "${uiState.averageSessionDurationMinutes} min",
                    icon = Icons.Default.History,
                    color = MaterialTheme.colorScheme.tertiary
                )
                
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun AnalyticsCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: ImageVector,
    color: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(icon, contentDescription = null, tint = color)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = title, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(text = value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = color)
        }
    }
}

@Composable
fun StatRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
        Text(text = value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
    }
}
