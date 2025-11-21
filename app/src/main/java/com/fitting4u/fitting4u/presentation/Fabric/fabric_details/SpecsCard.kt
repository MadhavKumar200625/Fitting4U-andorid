package com.fitting4u.fitting4u.presentation.Fabric.fabric_details

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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fitting4u.fitting4u.Data.remote.dto.fabric.Fabric.FabricDto


@Composable
fun SpecItem(label: String, value: String) {
    val primaryBlue = Color(0xFF003466)

    Column(
        modifier = Modifier.fillMaxWidth(0.5f)
    ) {
        Text(
            text = label,
            color = Color(0xFF6B7280),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium
            )
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium.copy(
                color = primaryBlue,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

/* ===================== Specs Card ===================== */
@Composable
fun SpecsCard(fabric: FabricDto) {

    val primaryBlue = Color(0xFF003466)
    val accentPink = Color(0xFFFFC1CC)

    Card(
        modifier = Modifier.Companion
            .padding(horizontal = 20.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Companion.White
        )
    ) {

        Column(
            modifier = Modifier.Companion
                .background(
                    brush = Brush.Companion.verticalGradient(
                        colors = listOf(
                            Color.Companion.White,
                            Color(0xFFF9FAFB),
                            Color(0xFFF4F7FA)
                        )
                    )
                )
                .padding(20.dp)
        ) {

            // 🌈 Heading with accent bar
            Row(
                verticalAlignment = Alignment.Companion.CenterVertically
            ) {
                Box(
                    modifier = Modifier.Companion
                        .width(4.dp)
                        .height(24.dp)
                        .background(
                            primaryBlue,
                            androidx.compose.foundation.shape.RoundedCornerShape(50)
                        )
                )
                Spacer(Modifier.Companion.width(10.dp))
                Text(
                    text = "Specifications",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = primaryBlue,
                        fontWeight = FontWeight.Companion.SemiBold
                    )
                )
            }

            Spacer(Modifier.Companion.height(18.dp))

            // =============================
            // 🔵 FIRST ROW
            // =============================
            Row(
                modifier = Modifier.Companion.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SpecItem(
                    label = "Width",
                    value = if (fabric.width > 0) "${fabric.width} in" else "—"
                )
                SpecItem(
                    label = "Material",
                    value = if (fabric.material.isNotBlank()) fabric.material else "—"
                )
            }

            Divider(
                modifier = Modifier.Companion
                    .padding(vertical = 14.dp),
                color = Color(0xFFE5E7EB)
            )

            // =============================
            // 🔵 SECOND ROW
            // =============================
            Row(
                modifier = Modifier.Companion.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SpecItem(
                    label = "Weave",
                    value = if (fabric.weave.isNotBlank()) fabric.weave else "—"
                )
                SpecItem(
                    label = "Color",
                    value = if (fabric.color.isNotBlank()) fabric.color else "—"
                )
            }
        }
    }
}