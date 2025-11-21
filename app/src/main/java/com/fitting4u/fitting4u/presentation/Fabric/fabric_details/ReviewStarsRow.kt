package com.fitting4u.fitting4u.presentation.Fabric.fabric_details

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.fitting4u.fitting4u.Data.remote.dto.fabric.Fabric.Review

/* ===================== Review Stars Row ===================== */
@Composable
 fun ReviewStarsRow(avgStars: Double, reviews: List<Review>) {
    val stars = (1..5).map { idx ->
        when {
            avgStars >= idx -> Icons.Default.Star
            avgStars > idx - 1 -> Icons.Default.StarHalf // fallback to half icon if you have
            else -> Icons.Default.StarOutline
        }
    }

    Row(
        modifier = Modifier.Companion.padding(horizontal = 20.dp, vertical = 6.dp),
        verticalAlignment = Alignment.Companion.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.Companion.CenterVertically) {
            stars.forEachIndexed { i, icon ->
                Icon(
                    icon,
                    contentDescription = null,
                    tint = Color(0xFFFFC107),
                    modifier = Modifier.Companion.size(18.dp)
                )
                Spacer(modifier = Modifier.Companion.width(4.dp))
            }
        }
        Spacer(modifier = Modifier.Companion.width(8.dp))
        Text(
            "(${reviews.size} reviews)",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF6B7280)
        )
    }
}