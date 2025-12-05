package com.fitting4u.fitting4u.presentation.Fabric.boutique.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Pagination(page: Int, totalPages: Int, onPrev: () -> Unit, onNext: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            onClick = onPrev,
            enabled = page > 1,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF003466))
        ) { Text("Previous", color = Color.White) }

        Text("Page $page of $totalPages", color = Color.Black)

        Button(
            onClick = onNext,
            enabled = page < totalPages,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF003466))
        ) { Text("Next", color = Color.White) }
    }
}