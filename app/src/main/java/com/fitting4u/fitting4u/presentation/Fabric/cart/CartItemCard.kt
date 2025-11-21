package com.fitting4u.fitting4u.presentation.Fabric.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.fitting4u.fitting4u.ui.theme.PrimaryBlue

@Composable
fun CartItemCard(
    id: String,
    name: String,
    image: String?,
    price: Double,
    material: String?,
    subtotal: Double,
    qty: Double,   // ⭐ NEW → show qty
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // -------------------------------
            // ⭐ PRODUCT IMAGE
            // -------------------------------
            Image(
                painter = rememberAsyncImagePainter(image),
                contentDescription = name,
                modifier = Modifier
                    .size(95.dp)
                    .clip(RoundedCornerShape(14.dp)),
                contentScale = ContentScale.Crop
            )

            // -------------------------------
            // ⭐ DETAILS + QTY + SUBTOTAL
            // -------------------------------
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                // Name + material
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = PrimaryBlue
                )

                if (!material.isNullOrBlank()) {
                    Text(
                        text = material,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF6B7280)
                    )
                }

                Spacer(Modifier.height(6.dp))

                // Price per meter
                Text(
                    text = "₹$price / meter",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )

                Spacer(Modifier.height(10.dp))

                // -------------------------------
                // ⭐ QTY CONTROL ROW
                // -------------------------------
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDecrease,
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(6.dp),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Text("-", style = MaterialTheme.typography.titleLarge)
                    }

                    Text(
                        text = "${"%.2f".format(qty)} m",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = PrimaryBlue
                    )

                    OutlinedButton(
                        onClick = onIncrease,
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(6.dp),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Text("+", style = MaterialTheme.typography.titleLarge)
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Remove button
                    IconButton(onClick = onRemove) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Remove",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            // -------------------------------
            // ⭐ SUBTOTAL (Right Side)
            // -------------------------------
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "₹${"%.2f".format(subtotal)}",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = PrimaryBlue
                )
                Text(
                    "Subtotal",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF6B7280)
                )
            }
        }
    }
}