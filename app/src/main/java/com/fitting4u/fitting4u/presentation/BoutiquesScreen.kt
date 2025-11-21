package com.fitting4u.fitting4u.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun BoutiquesScreen() {
    Box(
        modifier = Modifier.fillMaxSize()
            .background(Color.Blue),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Boutiques",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}