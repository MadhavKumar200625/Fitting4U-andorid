package com.fitting4u.fitting4u.presentation.Fabric.Home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FabricSearchBar(onClick: () -> Unit) {

    val primaryBlue = Color(0xFF003466)
    val accentPink = Color(0xFFFFC1CC)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(22.dp),
                clip = false
            )
            .clip(RoundedCornerShape(22.dp))
            .background(Color.White)    // SINGLE uniform white background
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    listOf(
                        primaryBlue.copy(alpha = 0.5f),
                        accentPink.copy(alpha = 0.9f)
                    )
                ),
                shape = RoundedCornerShape(22.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {

            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = primaryBlue.copy(alpha = 0.75f),
                modifier = Modifier.size(20.dp)
            )

            Spacer(Modifier.width(10.dp))

            Text(
                text = "Search fabrics, prints, colors…",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = primaryBlue.copy(alpha = 0.55f)
                )
            )
        }
    }
}