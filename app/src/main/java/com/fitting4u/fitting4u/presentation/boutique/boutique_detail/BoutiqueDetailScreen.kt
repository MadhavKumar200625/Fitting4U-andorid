package com.fitting4u.fitting4u.presentation.boutique.boutique_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun BoutiqueDetailScreen(
    id: String,
    vm: BoutiqueDetailViewModel = hiltViewModel(),
    onNavigateToBoutiqueSearch: () -> Unit = {}
) {
    val uiStateFlow = remember(vm) { vm.ui }
    val ui by uiStateFlow.value.let { mutableStateOf(it) }

    // Load once
    LaunchedEffect(id) {
        vm.load(id)
    }

    when {
        ui.loading -> {
            BoutiqueDetailShimmer()
        }

        ui.error != null -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = ui.error ?: "Something went wrong",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = { vm.load(id) }) {
                        Text("Retry")
                    }
                }
            }
        }

        ui.detail != null -> {
            BoutiqueDetailContent(
                data = ui.detail!!.data,
                nearMe = ui.detail!!.nearMe,
                related = ui.detail!!.related,
                onNavigateToBoutiqueSearch = onNavigateToBoutiqueSearch
            )
        }
    }
}

@Composable
fun BoutiqueDetailShimmer() {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        repeat(6) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .padding(vertical = 8.dp)
                    .background(Color.LightGray.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            )
        }
    }
}