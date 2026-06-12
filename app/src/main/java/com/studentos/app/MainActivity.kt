package com.studentos.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.studentos.app.navigation.AppNavigation
import com.studentos.app.ui.theme.StudentOSTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StudentOSTheme {
                AppNavigation(navController = rememberNavController())
            }
        }
    }
}