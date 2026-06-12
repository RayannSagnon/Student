package com.studentos.app.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.studentos.app.navigation.Screen
import com.studentos.app.ui.screens.*

data class BottomNavItem(
    val label: String,
    val route: String,
    val icon: ImageVector
)

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    var showQuickActions by remember { mutableStateOf(false) }

    val items = listOf(
        BottomNavItem("Home", Screen.Dashboard.route, Icons.Outlined.Home),
        BottomNavItem("Courses", Screen.Courses.route, Icons.Outlined.School),
        BottomNavItem("Tasks", Screen.Assignments.route, Icons.Outlined.Assignment),
        BottomNavItem("Profile", Screen.Profile.route, Icons.Outlined.Person)
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isFullScreen = currentRoute == Screen.StudySession.route || 
                      currentRoute == Screen.AddAssignment.route || 
                      currentRoute == Screen.AddCourse.route ||
                      currentRoute == Screen.AcademicAnalytics.route ||
                      currentRoute == Screen.NotificationSettings.route ||
                      currentRoute == Screen.Settings.route

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            if (!isFullScreen) {
                Box(contentAlignment = Alignment.BottomEnd) {
                    val rotation by animateFloatAsState(
                        targetValue = if (showQuickActions) 45f else 0f, label = "fab_rotation"
                    )

                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(bottom = 16.dp, end = 16.dp)
                    ) {
                        if (showQuickActions) {
                            QuickActionsMenu(
                                onAddTask = {
                                    showQuickActions = false
                                    navController.navigate(Screen.AddAssignment.route)
                                },
                                onAddCourse = { 
                                    showQuickActions = false 
                                    navController.navigate(Screen.AddCourse.route)
                                },
                                onStartSession = {
                                    showQuickActions = false
                                    navController.navigate(Screen.StudySession.route)
                                }
                            )
                        }

                        FloatingActionButton(
                            onClick = { showQuickActions = !showQuickActions },
                            shape = CircleShape,
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = "Add",
                                modifier = Modifier.rotate(rotation)
                            )
                        }
                    }
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        bottomBar = {
            if (!isFullScreen) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    PremiumBottomBar(
                        items = items,
                        navController = navController
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(if (isFullScreen) PaddingValues(0.dp) else innerPadding)
        ) {
            composable(Screen.Dashboard.route) { DashboardScreen(navController) }
            composable(Screen.Courses.route) { CourseScreen(navController) }
            composable(Screen.Assignments.route) { AssignmentsScreen(navController) }
            composable(Screen.Profile.route) { ProfileScreen(navController) }
            composable(Screen.AddAssignment.route) {
                AddAssignmentScreen(onBack = { navController.popBackStack() })
            }
            composable(Screen.AddCourse.route) {
                AddCourseScreen(onBack = { navController.popBackStack() })
            }
            composable(Screen.StudySession.route) {
                StudySessionScreen(onBack = { navController.popBackStack() })
            }
            
            // Profile Sub-screens
            composable(Screen.AcademicAnalytics.route) {
                PlaceholderScreen("Academic Analytics", onBack = { navController.popBackStack() })
            }
            composable(Screen.NotificationSettings.route) {
                PlaceholderScreen("Notification Settings", onBack = { navController.popBackStack() })
            }
            composable(Screen.Settings.route) {
                PlaceholderScreen("Settings", onBack = { navController.popBackStack() })
            }
        }
    }
}

@Composable
fun PremiumBottomBar(
    items: List<BottomNavItem>,
    navController: NavHostController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Card(
        shape = RoundedCornerShape(32.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val selected = currentDestination?.hierarchy?.any {
                    it.route == item.route
                } == true

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable {
                            if (!selected) {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .alpha(if (selected) 1f else 0.65f)
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        modifier = Modifier.size(if (selected) 26.dp else 22.dp),
                        tint = if (selected)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (selected)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
fun QuickActionsMenu(
    onAddTask: () -> Unit,
    onAddCourse: () -> Unit,
    onStartSession: () -> Unit
) {
    AnimatedVisibility(
        visible = true,
        enter = slideInVertically { it } + fadeIn(),
        exit = slideOutVertically { it } + fadeOut()
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.98f)
            ),
            modifier = Modifier.width(200.dp)
        ) {
            Column(
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                QuickActionItem(
                    icon = Icons.Outlined.AddTask,
                    label = "New Task",
                    onClick = onAddTask
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                QuickActionItem(
                    icon = Icons.Outlined.MenuBook,
                    label = "New Course",
                    onClick = onAddCourse
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                QuickActionItem(
                    icon = Icons.Filled.PlayArrow,
                    label = "Start Session",
                    onClick = onStartSession
                )
            }
        }
    }
}

@Composable
fun QuickActionItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun PlaceholderScreen(title: String, onBack: () -> Unit) {
    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Outlined.Build,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "$title Screen",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "Coming Soon",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
