package com.studentos.app.navigation

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object Courses : Screen("courses")
    object Assignments : Screen("assignments")
    object Profile : Screen("profile")
    object AddAssignment : Screen("add_assignment")
    object AddCourse : Screen("add_course")
    object StudySession : Screen("study_session")
    object Login : Screen("login")
    object Signup : Screen("signup")
    
    // Profile Sub-screens
    object AcademicAnalytics : Screen("academic_analytics")
    object NotificationSettings : Screen("notification_settings")
    object Settings : Screen("settings")
}
