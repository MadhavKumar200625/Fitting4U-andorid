package com.fitting4u.fitting4u.presentation.Fabric.fabric_details

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fitting4u.fitting4u.Data.remote.dto.fabric.Fabric.FabricDto
import com.fitting4u.fitting4u.ui.theme.PrimaryBlue

/* ===================== Price Card ===================== */
@Composable
 fun PriceCard(context: Context, fabric: FabricDto, onAddToCart: ((String, Double) -> Unit)?) {
    val price = fabric.price
    val cust = fabric.customerPrice
    val discountPercent = if (price > 0) (((price - cust) / price) * 100).toInt() else 0

    Card(
        modifier = Modifier.Companion
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(14.dp)),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(14.dp)
    ) {
        Box(
            modifier = Modifier.Companion
                .background(Color.Companion.White)
                .padding(16.dp)
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.Companion.Bottom,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.Companion.fillMaxWidth()
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.Companion.Bottom) {
                            Text(
                                "₹${cust}",
                                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Companion.ExtraBold),
                                color = PrimaryBlue
                            )
                            Spacer(modifier = Modifier.Companion.width(8.dp))
                            if (price > 0) {
                                Text(
                                    "₹${price}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color(0xFF9CA3AF),
                                    modifier = Modifier.Companion.padding(top = 6.dp)
                                )
                            }
                        }
                        if (fabric.boutiquePrice > 0) {
                            Text(
                                "Boutique: ₹${fabric.boutiquePrice}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF374151)
                            )
                        }
                        Text(
                            "Inclusive of all taxes",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF6B7280)
                        )
                    }

                    // Discount badge
                    if (discountPercent > 0) {
                        Box(
                            modifier = Modifier.Companion
                                .clip(RoundedCornerShape(bottomStart = 12.dp, topEnd = 10.dp))
                                .background(PrimaryBlue)
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                "$discountPercent% OFF",
                                color = Color.Companion.White,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Companion.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.Companion.height(8.dp))

                Text(
                    text = if (fabric.stockLeft > 0) "${fabric.stockLeft} meters available" else "Out of Stock",
                    color = if (fabric.stockLeft > 0) Color(0xFF16A34A) else Color(0xFFDC2626),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}