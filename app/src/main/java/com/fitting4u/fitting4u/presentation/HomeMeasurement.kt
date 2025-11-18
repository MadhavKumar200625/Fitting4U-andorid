package com.fitting4u.fitting4u.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Alignment

@Composable
fun HomeMeasurementScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Home Measurement",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}