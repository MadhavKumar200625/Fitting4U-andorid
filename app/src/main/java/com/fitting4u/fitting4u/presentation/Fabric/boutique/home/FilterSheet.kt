package com.fitting4u.fitting4u.presentation.Fabric.boutique.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun FilterSheet(
    ui: BoutiqueSearchUiState,
    onApply: (String, String, String, String) -> Unit
) {
    var type by remember { mutableStateOf(ui.type) }
    var price by remember { mutableStateOf(ui.priceRange) }
    var verified by remember { mutableStateOf(ui.verified) }
    var location by remember { mutableStateOf(ui.location) }

    Column(Modifier.padding(20.dp)) {

        Text("Filters", fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.titleLarge.fontSize)

        Spacer(Modifier.height(20.dp))

        DropdownSelector("Boutique Type", type, listOf("All","Men","Women","Unisex")) { type = it }

        DropdownSelector("Price Range", price, listOf("All","Low","Medium","High","Luxury")) { price = it }

        DropdownSelector("Verified", verified, listOf("All","true","false")) { verified = it }

        DropdownSelector("Location", location, listOf(
            "All","Delhi","Mumbai","Bangalore","Pune","Chennai","Hyderabad","Kolkata","Jaipur"
        )) { location = it }

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = { onApply(type, price, verified, location) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF003466))
        ) {
            Text("Apply Filters", color = Color.White)
        }
    }
}

@Composable
fun DropdownSelector(
    label: String,
    selected: String,
    options: List<String>,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(label, color = Color.Gray)
        Box {
            OutlinedButton(
                onClick = { expanded = true },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(selected)
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                options.forEach {
                    DropdownMenuItem(
                        text = { Text(it) },
                        onClick = {
                            expanded = false
                            onSelect(it)
                        }
                    )
                }
            }
        }
    }
}