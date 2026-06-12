package com.studentos.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.studentos.app.data.Graph
import com.studentos.app.domain.model.Assignment
import com.studentos.app.navigation.Screen
import com.studentos.app.ui.components.EmptyState
import com.studentos.app.viewmodel.AssignmentViewModel
import com.studentos.app.viewmodel.CourseViewModel
import com.studentos.app.viewmodel.ViewModelFactory
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssignmentsScreen(
    navController: NavController,
    assignmentViewModel: AssignmentViewModel = viewModel(
        factory = ViewModelFactory(Graph.assignmentRepository, Graph.authRepository)
    ),
    courseViewModel: CourseViewModel = viewModel(
        factory = ViewModelFactory(Graph.courseRepository, Graph.authRepository)
    )
) {
    val assignments by assignmentViewModel.assignments.collectAsState()
    val courses by courseViewModel.courses.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "My Assignments",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                    ) 
                }
            )
        }
    ) { padding ->
        if (assignments.isEmpty()) {
            EmptyState(
                icon = Icons.Outlined.Assignment,
                message = "No assignments yet",
                description = "Keep track of your deadlines and tasks by adding your first assignment.",
                actionLabel = "Add Assignment",
                onAction = { navController.navigate(Screen.AddAssignment.route) }
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val grouped = assignments.groupBy { it.completed }
                
                val active = grouped[false] ?: emptyList()
                val completed = grouped[true] ?: emptyList()

                if (active.isNotEmpty()) {
                    item {
                        Text(
                            "Active",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                    items(active) { assignment ->
                        val course = courses.find { it.id == assignment.courseId }
                        AssignmentItem(
                            assignment = assignment,
                            courseName = course?.name ?: "Unknown",
                            courseColor = course?.color ?: "#808080",
                            onToggle = { assignmentViewModel.toggleAssignment(assignment) }
                        )
                    }
                }

                if (completed.isNotEmpty()) {
                    item {
                        Spacer(Modifier.height(16.dp))
                        Text(
                            "Completed",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                    items(completed) { assignment ->
                        val course = courses.find { it.id == assignment.courseId }
                        AssignmentItem(
                            assignment = assignment,
                            courseName = course?.name ?: "Unknown",
                            courseColor = course?.color ?: "#808080",
                            onToggle = { assignmentViewModel.toggleAssignment(assignment) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AssignmentItem(
    assignment: Assignment,
    courseName: String,
    courseColor: String,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onToggle) {
                Icon(
                    imageVector = if (assignment.completed) Icons.Default.CheckCircle else Icons.Outlined.RadioButtonUnchecked,
                    contentDescription = "Toggle completion",
                    tint = if (assignment.completed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = assignment.title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        textDecoration = if (assignment.completed) TextDecoration.LineThrough else null
                    ),
                    color = if (assignment.completed) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                )
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(Color(courseColor.toColorInt()), CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = courseName,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = assignment.dueDate.format(DateTimeFormatter.ofPattern("MMM dd")),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
