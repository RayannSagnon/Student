package com.studentos.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.studentos.app.data.Graph
import com.studentos.app.viewmodel.AssignmentViewModel
import com.studentos.app.viewmodel.CourseViewModel
import com.studentos.app.viewmodel.ViewModelFactory
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAssignmentScreen(
    onBack: () -> Unit,
    assignmentViewModel: AssignmentViewModel = viewModel(
        factory = ViewModelFactory(Graph.assignmentRepository, Graph.authRepository)
    ),
    courseViewModel: CourseViewModel = viewModel(
        factory = ViewModelFactory(Graph.courseRepository, Graph.authRepository)
    )
) {
    var title by remember { mutableStateOf("") }
    var selectedCourseId by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    
    val dateState = rememberDatePickerState(initialSelectedDateMillis = Instant.now().toEpochMilli())
    val timeState = rememberTimePickerState()
    
    var selectedDate by remember { mutableStateOf(LocalDateTime.now()) }

    val courses by courseViewModel.courses.collectAsState()

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    dateState.selectedDateMillis?.let {
                        val date = Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                        selectedDate = selectedDate.withYear(date.year).withMonth(date.monthValue).withDayOfMonth(date.dayOfMonth)
                    }
                    showDatePicker = false
                    showTimePicker = true
                }) { Text("Next") }
            }
        ) {
            DatePicker(state = dateState)
        }
    }

    if (showTimePicker) {
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    selectedDate = selectedDate.withHour(timeState.hour).withMinute(timeState.minute)
                    showTimePicker = false
                }) { Text("Confirm") }
            },
            text = {
                TimePicker(state = timeState)
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New Assignment") },
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
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Assignment Title") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Box {
                OutlinedTextField(
                    value = courses.find { it.id == selectedCourseId }?.name ?: "Select Course",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Course") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    }
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable { expanded = !expanded }
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
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

            OutlinedTextField(
                value = selectedDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy - HH:mm")),
                onValueChange = {},
                readOnly = true,
                label = { Text("Due Date & Time") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true },
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                shape = RoundedCornerShape(12.dp),
                trailingIcon = {
                    Icon(Icons.Default.CalendarToday, contentDescription = null)
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (title.isNotBlank() && selectedCourseId.isNotBlank()) {
                        assignmentViewModel.addAssignment(
                            title = title,
                            courseId = selectedCourseId,
                            dueDate = selectedDate
                        )
                        onBack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                enabled = title.isNotBlank() && selectedCourseId.isNotBlank()
            ) {
                Text("Save Assignment")
            }
        }
    }
}
