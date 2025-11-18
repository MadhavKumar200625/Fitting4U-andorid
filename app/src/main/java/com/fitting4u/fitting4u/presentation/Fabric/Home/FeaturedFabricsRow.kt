package com.fitting4u.fitting4u.presentation.Fabric.Home

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment

import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.fitting4u.fitting4u.Data.remote.dto.fabric.FabricHome.FeaturedFabric


@Composable
fun FeaturedFabricsRow(
    fabrics: List<FeaturedFabric>,
    navController: NavController
) {
    LazyRow(
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(fabrics) { item ->
            FeaturedFabricCard(item) {
                navController.navigate("fabricDetail/${item.slug}")
            }
        }
    }
}

@Composable
fun FeaturedFabricCard(
    item: FeaturedFabric,
    onClick: () -> Unit
) {
    val pureWhite = Color(0xFFFFFFFF)
    val primaryBlue = Color(0xFF003466)
    val accentPink = Color(0xFFFFC1CC)

    Box(
        modifier = Modifier
            .width(170.dp)
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(22.dp),
                clip = false
            )
            .clip(RoundedCornerShape(22.dp))
            .background(pureWhite) // ONE UNIFIED WHITE
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    listOf(
                        primaryBlue.copy(alpha = 0.08f),
                        accentPink.copy(alpha = 0.08f)
                    )
                ),
                shape = RoundedCornerShape(22.dp)
            )
            .clickable { onClick() }
    ) {

        Column {

            AsyncImage(
                model = item.images.firstOrNull(),
                contentDescription = item.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(190.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp))
                    .background(pureWhite) // same white
            )

            Column(modifier = Modifier.padding(14.dp)) {

                Text(
                    text = item.name,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = primaryBlue
                    ),
                    maxLines = 2
                )

                Spacer(Modifier.height(6.dp))


                Text(
                    text = "₹${item.customerPrice}",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color(0xFF008043),          // premium green price
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = item.avgStars.toString(),
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = primaryBlue.copy(alpha = 0.8f)
                        )
                    )
                }
            }
        }
    }
}