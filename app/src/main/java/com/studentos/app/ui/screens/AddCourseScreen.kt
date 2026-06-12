package com.studentos.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.viewmodel.compose.viewModel
import com.studentos.app.data.Graph
import com.studentos.app.domain.model.CourseDifficulty
import com.studentos.app.viewmodel.CourseViewModel
import com.studentos.app.viewmodel.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCourseScreen(
    onBack: () -> Unit,
    viewModel: CourseViewModel = viewModel(
        factory = ViewModelFactory(Graph.courseRepository, Graph.authRepository)
    )
) {
    var name by remember { mutableStateOf("") }
    var professor by remember { mutableStateOf("") }
    var courseCode by remember { mutableStateOf("") }
    var targetGrade by remember { mutableStateOf("") }
    var difficulty by remember { mutableStateOf(CourseDifficulty.MEDIUM) }
    var selectedColor by remember { mutableStateOf("#4CAF50") }

    val colors = listOf("#4CAF50", "#2196F3", "#FF9800", "#F44336", "#9C27B0", "#00BCD4", "#E91E63", "#795548")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New Course") },
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
                value = name,
                onValueChange = { name = it },
                label = { Text("Course Name *") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = courseCode,
                    onValueChange = { courseCode = it },
                    label = { Text("Code (e.g. CS101)") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                )
                OutlinedTextField(
                    value = targetGrade,
                    onValueChange = { targetGrade = it },
                    label = { Text("Target Grade") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            OutlinedTextField(
                value = professor,
                onValueChange = { professor = it },
                label = { Text("Professor Name *") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Text("Difficulty", style = MaterialTheme.typography.titleSmall)
            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                CourseDifficulty.entries.forEachIndexed { index, diff ->
                    SegmentedButton(
                        selected = difficulty == diff,
                        onClick = { difficulty = diff },
                        shape = SegmentedButtonDefaults.itemShape(index = index, count = CourseDifficulty.entries.size)
                    ) {
                        Text(diff.name.lowercase().replaceFirstChar { it.uppercase() })
                    }
                }
            }

            Text("Course Color", style = MaterialTheme.typography.titleSmall)
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(colors) { hex ->
                    val color = Color(hex.toColorInt())
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(color, CircleShape)
                            .clickable { selectedColor = hex }
                            .padding(2.dp)
                    ) {
                        if (selectedColor == hex) {
                            Surface(
                                modifier = Modifier.fillMaxSize(),
                                color = Color.Transparent,
                                shape = CircleShape,
                                border = androidx.compose.foundation.BorderStroke(2.dp, Color.White)
                            ) {}
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (name.isNotBlank() && professor.isNotBlank()) {
                        viewModel.addCourse(
                            name = name,
                            professor = professor,
                            color = selectedColor,
                            difficulty = difficulty,
                            courseCode = courseCode.ifBlank { null },
                            targetGrade = targetGrade.ifBlank { null }
                        )
                        onBack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                enabled = name.isNotBlank() && professor.isNotBlank()
            ) {
                Text("Save Course")
            }
        }
    }
}
