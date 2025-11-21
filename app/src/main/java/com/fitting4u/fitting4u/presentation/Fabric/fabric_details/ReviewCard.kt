package com.fitting4u.fitting4u.presentation.Fabric.fabric_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fitting4u.fitting4u.Data.remote.dto.fabric.Fabric.Review
import com.fitting4u.fitting4u.ui.theme.PrimaryBlue

/* ===================== Review Card ===================== */
@Composable
fun ReviewCard(r: Review) {
    val primaryBlue = PrimaryBlue

    Card(
        modifier = Modifier.Companion
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Companion.White
        )
    ) {
        Column(
            modifier = Modifier.Companion
                .background(
                    Brush.Companion.verticalGradient(
                        listOf(
                            Color.Companion.White,
                            Color(0xFFF9FAFB),
                            Color(0xFFEFF4F8)
                        )
                    )
                )
                .padding(18.dp)
        ) {

            // ===================== Header Row =====================
            Row(
                verticalAlignment = Alignment.Companion.CenterVertically,
                modifier = Modifier.Companion.fillMaxWidth()
            ) {

                // Avatar circle with initials
                Box(
                    modifier = Modifier.Companion
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(primaryBlue.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Companion.Center
                ) {
                    Text(
                        (r.name.takeIf { it.isNotBlank() } ?: "A").uppercase().take(1),
                        fontWeight = FontWeight.Companion.Bold,
                        color = primaryBlue
                    )
                }

                Spacer(Modifier.Companion.width(12.dp))

                Column {
                    Text(
                        text = r.name.ifBlank { "Anonymous" },
                        fontWeight = FontWeight.Companion.SemiBold,
                        color = primaryBlue,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = r.createdAt.takeIf { it.isNotBlank() } ?: "Recently",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF6B7280)
                    )
                }
            }

            Spacer(Modifier.Companion.height(14.dp))

            // ===================== Stars Row =====================
            Row(verticalAlignment = Alignment.Companion.CenterVertically) {
                repeat(r.stars.toInt()) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.Companion.size(20.dp)
                    )
                }
                repeat((5 - r.stars.toInt()).coerceAtLeast(0)) {
                    Icon(
                        Icons.Default.StarOutline,
                        contentDescription = null,
                        tint = Color(0xFFD1D5DB),
                        modifier = Modifier.Companion.size(20.dp)
                    )
                }

                Spacer(Modifier.Companion.width(6.dp))

                Text(
                    "${r.stars}/5",
                    fontWeight = FontWeight.Companion.Medium,
                    color = Color(0xFF374151)
                )
            }

            Spacer(Modifier.Companion.height(14.dp))

            // ===================== Review Text =====================
            Text(
                text = "“${r.review.trim()}”",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFF374151),
                    fontWeight = FontWeight.Companion.Normal,
                    lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.25
                )
            )
        }
    }
}