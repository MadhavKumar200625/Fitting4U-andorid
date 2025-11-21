package com.fitting4u.fitting4u.presentation.Fabric.explore

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.fitting4u.fitting4u.Data.remote.dto.fabric.explore.Fabric

@Composable
fun FabricList(
    state: ExploreUiState,
    gridState: LazyGridState,
    onItemClick: (Fabric) -> Unit
) {
    LazyVerticalGrid(
        state = gridState,
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {

        /** MAIN GRID ITEMS **/
        items(
            items = state.fabrics,
            key = { it.slug }, // stable keys for better pagination
        ) { item ->
            FabricCard(
                item = item,
                onClick = { onItemClick(item) }
            )
        }

        /** PAGINATION LOADER (Spans 2 Columns) **/
        item(span = { GridItemSpan(maxLineSpan) }) {
            if (state.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF003466))
                }
            } else {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

/* ------------------------------------------------------
   FABRIC CARD
-------------------------------------------------------*/
@Composable
fun FabricCard(
    item: Fabric,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(18.dp),
                spotColor = Color(0x22000000)
            )
            .clip(RoundedCornerShape(18.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {

            // IMAGE
            AsyncImage(
                model = item.images.firstOrNull(),
                contentDescription = item.name,
                modifier = Modifier
                    .height(160.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.height(12.dp))

            // TITLE
            Text(
                text = item.name,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF222222)
                )
            )

            Spacer(Modifier.height(4.dp))

            // SUBTITLE
            Text(
                text = item.material,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color(0xFF666666)
                ),
                maxLines = 2
            )

            Spacer(Modifier.height(10.dp))

            // PRICE + RATING
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = "₹${item.customerPrice.toInt()}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111111)
                    )
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = item.avgStars.toString(),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color(0xFF444444)
                        )
                    )
                }
            }
        }
    }
}