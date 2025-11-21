package com.fitting4u.fitting4u.presentation.Fabric.fabric_details

import androidx.compose.foundation.background
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DescriptionCard(text: String) {

    val primaryBlue = Color(0xFF003466)

    Card(
        modifier = Modifier.Companion
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(18.dp),
                ambientColor = Color.Companion.Black.copy(alpha = 0.10f),
                spotColor = Color.Companion.Black.copy(alpha = 0.20f)
            ),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Companion.White)
    ) {

        Column(
            modifier = Modifier.Companion
                .background(
                    Brush.Companion.verticalGradient(
                        listOf(
                            Color.Companion.White,
                            Color(0xFFF9FAFB),
                            Color(0xFFF4F7FA)
                        )
                    )
                )
                .padding(20.dp)
        ) {

            // Title + Accent Bar
            Row(verticalAlignment = Alignment.Companion.CenterVertically) {
                Box(
                    modifier = Modifier.Companion
                        .width(4.dp)
                        .height(26.dp)
                        .background(
                            primaryBlue,
                            androidx.compose.foundation.shape.RoundedCornerShape(50)
                        )
                )
                Spacer(Modifier.Companion.width(12.dp))
                Text(
                    text = "Description",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Companion.SemiBold,
                        color = primaryBlue
                    )
                )
            }

            Spacer(Modifier.Companion.height(14.dp))

            // Description Text
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge.copy(
                    lineHeight = 24.sp,
                    color = Color(0xFF374151),
                    fontWeight = FontWeight.Companion.Medium
                )
            )
        }
    }
}