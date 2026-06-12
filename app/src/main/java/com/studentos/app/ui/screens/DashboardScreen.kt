package com.studentos.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.DashboardCustomize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.studentos.app.data.Graph
import com.studentos.app.domain.model.Assignment
import com.studentos.app.navigation.Screen
import com.studentos.app.ui.components.EmptyState
import com.studentos.app.viewmodel.DashboardViewModel
import com.studentos.app.viewmodel.MultiRepositoryProvider
import com.studentos.app.viewmodel.ViewModelFactory
import java.time.format.DateTimeFormatter

@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: DashboardViewModel = viewModel(
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

    if (uiState.activeTaskCount == 0 && uiState.nextCourse == null && !uiState.isLoading) {
        EmptyState(
            icon = Icons.Outlined.DashboardCustomize,
            message = "Your dashboard is empty",
            description = "Start your academic journey by adding courses and assignments.",
            actionLabel = "Get Started",
            onAction = { navController.navigate(Screen.AddCourse.route) }
        )
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(top = 20.dp, bottom = 100.dp)
        ) {
            item { HeaderSection() }

            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    WeeklyProgressCard(modifier = Modifier.weight(1f), progress = uiState.weeklyProgress)
                    StreakCard(modifier = Modifier.weight(1f), streak = uiState.studyStreak)
                }
            }

            item {
                StudySessionCard(
                    nextCourseName = uiState.nextCourse?.name ?: "your courses",
                    onStartSession = { navController.navigate(Screen.StudySession.route) }
                )
            }

            if (uiState.recommendedTasks.isNotEmpty()) {
                item {
                    SectionHeader(
                        title = "Today's Study Plan",
                        onSeeAll = { navController.navigate(Screen.Assignments.route) }
                    )
                }
                items(uiState.recommendedTasks.take(2)) { assignment ->
                    AssignmentCard(assignment = assignment)
                }
            }

            item {
                SectionHeader(
                    title = "Upcoming Deadlines",
                    onSeeAll = { navController.navigate(Screen.Assignments.route) }
                )
            }

            items(uiState.upcomingAssignments) { assignment ->
                AssignmentCard(assignment = assignment)
            }
        }
    }
}

@Composable
fun HeaderSection() {
    Column {
        Text(
            text = "StudentOS",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = (-1).sp
            ),
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Your academic command center",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun WeeklyProgressCard(modifier: Modifier = Modifier, progress: Float) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(Icons.Default.AutoGraph, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Weekly Progress", style = MaterialTheme.typography.labelSmall)
            Text(text = "${(progress * 100).toInt()}%", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp).clip(CircleShape),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)
            )
        }
    }
}

@Composable
fun StreakCard(modifier: Modifier = Modifier, streak: Int) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(Icons.Default.LocalFireDepartment, contentDescription = null, tint = Color(0xFFFF5722))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Study Streak", style = MaterialTheme.typography.labelSmall)
            Row(verticalAlignment = Alignment.Bottom) {
                Text(text = streak.toString(), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(text = " days", style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(bottom = 4.dp))
            }
        }
    }
}

@Composable
fun StudySessionCard(nextCourseName: String, onStartSession: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Row(
            modifier = Modifier.padding(20.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Focus on $nextCourseName",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "Ready for a 25-min session?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
            }
            
            Button(
                onClick = onStartSession,
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null)
                Spacer(Modifier.width(4.dp))
                Text("Start")
            }
        }
    }
}

@Composable
fun SectionHeader(title: String, onSeeAll: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )
        TextButton(onClick = onSeeAll) {
            Text("See all")
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
fun AssignmentCard(assignment: Assignment) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(12.dp).background(MaterialTheme.colorScheme.primary, CircleShape))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = assignment.title,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Schedule, contentDescription = null, modifier = Modifier.size(12.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "Due " + assignment.dueDate.format(DateTimeFormatter.ofPattern("MMM dd, HH:mm")),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
