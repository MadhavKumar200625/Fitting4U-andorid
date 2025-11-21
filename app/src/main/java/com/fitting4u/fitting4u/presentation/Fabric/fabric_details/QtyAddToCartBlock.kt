package com.fitting4u.fitting4u.presentation.Fabric.fabric_details

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fitting4u.fitting4u.Data.remote.dto.fabric.Fabric.FabricDto
import com.fitting4u.fitting4u.ui.theme.PrimaryBlue
import kotlin.text.format

/* ===================== Qty + Add to Cart Block (inside scroll) ===================== */
@Composable
fun QtyAddToCartBlock(
    fabric: FabricDto,
    onAddToCart: (String, Double) -> Unit,
    onDecreaseQty: (String, Double) -> Unit,
    onRemoveFromCart: (String) -> Unit,
    existingQty: Double
) {
    var qty by rememberSaveable { mutableStateOf(1.0) }

    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)) {

        // --- Qty selector ---
        Row(verticalAlignment = Alignment.CenterVertically) {

            OutlinedButton(onClick = {
                qty = (qty - 0.25).coerceAtLeast(0.25)
            }) {
                Icon(Icons.Default.Remove, contentDescription = null)
            }

            Text(
                text = "${"%.2f".format(qty)} m",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            OutlinedButton(onClick = { qty += 0.25 }) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        }

        // 👉 NEW LINE: show existing qty under selector
        if (existingQty > 0) {
            Spacer(Modifier.height(6.dp))
            Text(
                text = "Currently in cart: ${"%.2f".format(existingQty)} m",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF6B7280), // soft gray
                modifier = Modifier.padding(start = 4.dp)
            )
        }

        Spacer(Modifier.height(16.dp))

        // --- Add to Cart or Update buttons ---
        if (existingQty > 0) {
            Button(
                onClick = { onAddToCart(fabric._id, qty) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
            ) {
                Text("Update Cart")
            }

            Spacer(Modifier.height(8.dp))

            OutlinedButton(
                onClick = { onDecreaseQty(fabric._id, 0.25) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Reduce 0.25 m")
            }

            Spacer(Modifier.height(8.dp))

            OutlinedButton(
                onClick = { onRemoveFromCart(fabric._id) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
            ) {
                Text("Remove from Cart")
            }

        } else {
            // Not in cart yet — show Add To Cart
            Button(
                onClick = { onAddToCart(fabric._id, qty) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
            ) {
                Text("Add to Cart")
            }
        }
    }
}