package com.studentos.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.studentos.app.data.Graph
import com.studentos.app.navigation.Screen
import com.studentos.app.viewmodel.AuthViewModel
import com.studentos.app.viewmodel.MultiRepositoryProvider
import com.studentos.app.viewmodel.ProfileViewModel
import com.studentos.app.viewmodel.ViewModelFactory
import java.util.Locale

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = viewModel(
        factory = ViewModelFactory(
            MultiRepositoryProvider(
                Graph.courseRepository,
                Graph.assignmentRepository,
                Graph.studySessionRepository
            ),
            Graph.authRepository
        )
    ),
    authViewModel: AuthViewModel = viewModel(factory = ViewModelFactory(Graph.authRepository))
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        
        // Profile Image Placeholder
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "RS",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Rayann Sagnon",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = "Software Engineering Student",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Stats Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ProfileStat(label = "Courses", value = uiState.courseCount.toString())
            ProfileStat(label = "Tasks", value = uiState.completedTasks.toString())
            ProfileStat(label = "Focus hrs", value = String.format(Locale.getDefault(), "%.1f", uiState.focusHours))
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Settings Options
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            ProfileOption(
                icon = Icons.Outlined.Analytics, 
                label = "Academic Analytics",
                onClick = { navController.navigate(Screen.AcademicAnalytics.route) }
            )
            ProfileOption(
                icon = Icons.Outlined.Notifications, 
                label = "Notifications",
                onClick = { navController.navigate(Screen.NotificationSettings.route) }
            )
            ProfileOption(
                icon = Icons.Outlined.Settings, 
                label = "Settings",
                onClick = { navController.navigate(Screen.Settings.route) }
            )
            ProfileOption(
                icon = Icons.AutoMirrored.Outlined.Logout,
                label = "Sign Out",
                color = MaterialTheme.colorScheme.error,
                onClick = {
                    authViewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}

@Composable
fun ProfileStat(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun ProfileOption(
    icon: ImageVector,
    label: String,
    color: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = label, style = MaterialTheme.typography.bodyLarge, color = color)
        }
    }
}
