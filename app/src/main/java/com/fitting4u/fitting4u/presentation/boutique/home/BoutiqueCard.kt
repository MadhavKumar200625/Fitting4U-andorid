package com.fitting4u.fitting4u.presentation.boutique.home


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.fitting4u.fitting4u.Data.remote.dto.boutique.search.Boutique

private val Primary = Color(0xFF003466)
private val Accent = Color(0xFFFFC1CC)

@Composable
fun BoutiqueCard(
    b: Boutique,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
            .clip(RoundedCornerShape(22.dp))
            .shadow(12.dp, RoundedCornerShape(22.dp))
            .background(Color.White)
            .clickable { onClick(b.websiteUrl) }
    ) {

        Row(Modifier.fillMaxSize()) {

            // LEFT IMAGE
            AsyncImage(
                model = b.businessLogo,
                contentDescription = b.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(135.dp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(22.dp))
            )

            // RIGHT SIDE CONTENT
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = 14.dp, end = 16.dp, top = 12.dp, bottom = 12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                // TITLE + VERIFIED BADGE
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        b.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Primary
                        ),
                        maxLines = 1,
                        modifier = Modifier.weight(1f)
                    )

                    if (b.verified) {
                        Box(
                            modifier = Modifier
                                .padding(start = 6.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Primary.copy(alpha = 0.12f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                "Verified",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Primary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                    }
                }

                // TAGLINE
                Text(
                    b.tagline,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color.Black.copy(alpha = 0.65f)
                    ),
                    maxLines = 1
                )

                // ADDRESS
                Text(
                    b.googleAddress,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Color.Gray
                    ),
                    maxLines = 1
                )

                Spacer(Modifier.height(4.dp))

                // TYPE + PRICE RANGE
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Type
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(Accent.copy(alpha = 0.2f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            b.type,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Primary,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }

                    Spacer(Modifier.width(10.dp))

                    // Price Range
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFEEF3FF))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            b.priceRange,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Primary,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }
            }
        }

        // Gradient Overlay for luxury ad-like vibe
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.White.copy(alpha = 0.03f))
                    )
                )
        )
    }
}