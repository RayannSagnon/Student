package com.studentos.app.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.studentos.app.data.Graph
import com.studentos.app.viewmodel.CourseViewModel
import com.studentos.app.viewmodel.StudySessionViewModel
import com.studentos.app.viewmodel.ViewModelFactory
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudySessionScreen(
    onBack: () -> Unit,
    courseViewModel: CourseViewModel = viewModel(
        factory = ViewModelFactory(Graph.courseRepository, Graph.authRepository)
    ),
    sessionViewModel: StudySessionViewModel = viewModel(
        factory = ViewModelFactory(Graph.studySessionRepository, Graph.authRepository)
    )
) {
    val courses by courseViewModel.courses.collectAsState()
    var selectedCourseId by remember { mutableStateOf("") }
    
    var totalTime by remember { mutableStateOf(25 * 60) }
    var timeLeft by remember { mutableStateOf(totalTime) }
    var isRunning by remember { mutableStateOf(false) }
    var isFinished by remember { mutableStateOf(false) }

    val startTime = remember { LocalDateTime.now() }

    LaunchedEffect(isRunning) {
        while (isRunning && timeLeft > 0) {
            delay(1000L)
            timeLeft--
            if (timeLeft == 0) {
                isRunning = false
                isFinished = true
                if (selectedCourseId.isNotEmpty()) {
                    sessionViewModel.completeSession(selectedCourseId, startTime, LocalDateTime.now())
                }
            }
        }
    }

    val progress = timeLeft.toFloat() / totalTime.toFloat()
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        label = "timer_progress"
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Study Session", style = MaterialTheme.typography.titleMedium) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            // Course Selection
            var expanded by remember { mutableStateOf(false) }
            Box {
                Surface(
                    onClick = { if (!isRunning) expanded = true },
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = courses.find { it.id == selectedCourseId }?.name ?: "Select a course",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                        )
                        Icon(
                            imageVector = Icons.Default.PlayArrow, 
                            contentDescription = null, 
                            modifier = Modifier
                                .size(16.dp)
                                .rotate(90f)
                        )
                    }
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    courses.forEach { course ->
                        DropdownMenuItem(
                            text = { Text(course.name) },
                            onClick = {
                                selectedCourseId = course.id
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Timer Visual
            Box(contentAlignment = Alignment.Center) {
                val color = MaterialTheme.colorScheme.primary
                val trackColor = MaterialTheme.colorScheme.surfaceVariant
                
                Canvas(modifier = Modifier.size(280.dp)) {
                    drawCircle(color = trackColor, style = Stroke(width = 12.dp.toPx()))
                    drawArc(
                        color = color,
                        startAngle = -90f,
                        sweepAngle = 360f * animatedProgress,
                        useCenter = false,
                        style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
                    )
                }
                
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    val minutes = timeLeft / 60
                    val seconds = timeLeft % 60
                    Text(
                        text = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds),
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontSize = 64.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = if (isRunning) "FOCUSING" else "PAUSED",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LargeFloatingActionButton(
                    onClick = { isRunning = !isRunning },
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        imageVector = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isRunning) "Pause" else "Start",
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
            
            if (isFinished) {
                Text(
                    "Great job! Session completed.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
                Button(onClick = onBack) {
                    Text("Finish")
                }
            } else {
                TextButton(onClick = { timeLeft = totalTime; isRunning = false }) {
                    Text("Reset Timer")
                }
            }
        }
    }
}
