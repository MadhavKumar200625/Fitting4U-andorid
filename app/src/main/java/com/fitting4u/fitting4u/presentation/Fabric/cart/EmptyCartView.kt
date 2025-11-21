package com.fitting4u.fitting4u.presentation.Fabric.cart

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EmptyCartView(onBrowse: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Your cart is empty", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))
        Text("Add premium fabrics to start building your look.", style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(16.dp))
        Button(onClick = onBrowse) {
            Text("Browse Fabrics")
        }
    }
}