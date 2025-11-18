package com.fitting4u.fitting4u.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AccountScreen() {

    var showLogin by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Text(
                text = "Account",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = { showLogin = true }
            ) {
                Text("Login")
            }
        }
    }

    // Show the popup when triggered
    if (showLogin) {
        LoginPopup(
            onDismiss = { showLogin = false },
            onSuccess = {
                showLogin = false
                println("User logged in successfully!")
            }
        )
    }
}