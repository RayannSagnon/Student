package com.studentos.app.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.studentos.app.data.Graph
import com.studentos.app.ui.auth.LoginScreen
import com.studentos.app.ui.auth.SignupScreen
import com.studentos.app.ui.screens.*
import com.studentos.app.viewmodel.AuthState
import com.studentos.app.viewmodel.AuthViewModel
import com.studentos.app.viewmodel.ViewModelFactory

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = viewModel(factory = ViewModelFactory(Graph.authRepository))
) {
    val authState by authViewModel.authState.collectAsState()

    val startDestination = if (authState is AuthState.Authenticated) {
        Screen.Dashboard.route
    } else {
        Screen.Login.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLogin = { email, password -> authViewModel.login(email, password) },
                onNavigateToSignup = { navController.navigate(Screen.Signup.route) }
            )
        }

        composable(Screen.Signup.route) {
            SignupScreen(
                onSignup = { email, password -> authViewModel.signUp(email, password) },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }

        composable(Screen.Dashboard.route) {
            DashboardScreen(navController = navController)
        }

        composable(Screen.Courses.route) {
            CourseScreen(navController = navController)
        }

        composable(Screen.Assignments.route) {
            AssignmentsScreen(navController = navController)
        }

        composable(Screen.Profile.route) {
            ProfileScreen(navController = navController)
        }

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
            AcademicAnalyticsScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.NotificationSettings.route) {
            // Notifications are now part of Settings, but we can keep the route for now or redirect
            SettingsScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Settings.route) {
            SettingsScreen(onBack = { navController.popBackStack() })
        }
    }
}
