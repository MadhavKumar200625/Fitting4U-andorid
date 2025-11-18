package com.fitting4u.fitting4u.presentation.Fabric.Home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun FabricTopBar(onSearchClick: () -> Unit) {

    val primaryBlue = Color(0xFF003466)
    val accentPink = Color(0xFFFFC1CC)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        accentPink.copy(alpha = 0.95f),
                        accentPink.copy(alpha = 0.75f),
                        accentPink.copy(alpha = 0.20f),
                        accentPink.copy(alpha = 0.05f),
                        MaterialTheme.colorScheme.background
                    )
                )
            )
            .padding(horizontal = 20.dp, vertical = 28.dp)
    ) {

        Column {

            Text(
                text = "Fabrics",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = primaryBlue
                )
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = "Premium fabrics curated for you",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = primaryBlue.copy(alpha = 0.7f)
                )
            )

            Spacer(Modifier.height(18.dp))

            // 🔥 THE MYNTRA STYLE ROW
            SearchAndIconsRow(onSearchClick, primaryBlue)
        }
    }
}
@Composable
fun SearchAndIconsRow(
    onSearchClick: () -> Unit,
    primaryBlue: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Search bar takes most space
        Box(modifier = Modifier.weight(1f)) {
            FabricSearchBar(onSearchClick)
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Heart icon
        Icon(
            imageVector = Icons.Default.FavoriteBorder,
            contentDescription = "Wishlist",
            tint = primaryBlue,
            modifier = Modifier.size(26.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Cart icon
        Icon(
            imageVector = Icons.Default.ShoppingCart,
            contentDescription = "Cart",
            tint = primaryBlue,
            modifier = Modifier.size(26.dp)
        )
    }
}