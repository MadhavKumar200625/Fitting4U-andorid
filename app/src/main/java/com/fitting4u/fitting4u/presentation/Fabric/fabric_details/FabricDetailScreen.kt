package com.fitting4u.fitting4u.presentation.Fabric.fabric_details

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.fitting4u.fitting4u.presentation.Fabric.CartViewModel

@Composable
fun FabricDetailScreen(
    id: String,
    navController: NavController,
    vm: FabricDetailViewModel = hiltViewModel(),
    cartVM: CartViewModel
) {
    val state by vm.state.collectAsState()
    val totalQty by cartVM.totalQty.collectAsState()

    LaunchedEffect(id) {
        vm.loadFabric(id)
    }

    when {
        state.isLoading -> {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        state.error != null -> {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                Text("Error: ${state.error}")
            }
        }

        state.fabric != null -> {
            val existingQty by cartVM.observeItemQty(state.fabric!!._id)
                .collectAsState(initial = 0.0)

            FabricDetailContent(
                fabric = state.fabric!!,

                // ACTIONS FROM CART VM
                onAddToCart = { fid, qty ->
                    cartVM.add(fid, qty)
                },

                onDecreaseQty = { fid, minus ->
                    cartVM.decreaseQty(fid, minus)
                },

                onRemoveFromCart = { fid ->
                    cartVM.removeItem(fid)
                },

                // QUERY
                existingQty = existingQty,
                totalQty = totalQty,
                navController = navController
            )
        }


    }

}