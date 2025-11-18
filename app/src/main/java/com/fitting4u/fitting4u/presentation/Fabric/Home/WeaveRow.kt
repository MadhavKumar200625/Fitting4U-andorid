package com.fitting4u.fitting4u.presentation.Fabric.Home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.Text
import com.fitting4u.fitting4u.Data.remote.dto.fabric.FabricHome.Weave

@Composable
fun WeaveRow(weaves: List<Weave>, navController: NavController) {
    LazyRow(
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        items(weaves) { weave ->
            Chip(
                label = { Text(weave.name) },
                onClick = { navController.navigate("fabricExplore?weave=${weave.name}") }
            )
        }
    }
}