package com.studentos.app.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(
    onLogin: (String, String) -> Unit,
    onNavigateToSignup: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("StudentOS", style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(32.dp))
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        OutlinedTextField(
            value = password, 
            onValueChange = { password = it }, 
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(Modifier.height(24.dp))
        Button(onClick = { onLogin(email, password) }, modifier = Modifier.fillMaxWidth()) {
            Text("Login")
        }
        TextButton(onClick = onNavigateToSignup) {
            Text("No account? Sign up")
        }
    }
}

@Composable
fun SignupScreen(
    onSignup: (String, String) -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Create Account", style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(32.dp))
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        OutlinedTextField(
            value = password, 
            onValueChange = { password = it }, 
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(Modifier.height(24.dp))
        Button(onClick = { onSignup(email, password) }, modifier = Modifier.fillMaxWidth()) {
            Text("Sign Up")
        }
        TextButton(onClick = onNavigateToLogin) {
            Text("Already have an account? Login")
        }
    }
}
