package com.fitting4u.fitting4u.presentation.Fabric.cart

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.fitting4u.fitting4u.presentation.Fabric.CartViewModel
import com.fitting4u.fitting4u.presentation.NavRoutes

@Composable
fun CartScreen(
    navController: NavController,
    vm: CartViewModel = hiltViewModel()
) {
    val state by vm.cartSummary.collectAsState()
    val totalQty by vm.totalQty.collectAsState()

    // load on enter
    LaunchedEffect(Unit) {
        vm.loadCartSummary()
        vm.refresh()
    }

    Box(modifier = Modifier.fillMaxSize().padding(bottom = 90.dp , top = 20.dp)) {
        when {
            state.loading -> {
                // Center loading
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            state.error != null -> {
                // Error view
                ErrorView(error = state.error ?: "Unknown") {
                    vm.loadCartSummary()
                }
            }

            state.data == null -> {
                EmptyCartView {
                    // navigate to explore/home
                    navController.navigate(NavRoutes.FabricExplore)
                }
            }

            else -> {
              // show cart
                CartContent(
                    data = state.data!!,
                    onIncrease = { id, deltaQty ->
                        vm.add(id, deltaQty)

                    },
                    onDecrease = { id, minus ->
                        vm.decreaseQty(id, minus)

                    },
                    onRemove = { id ->
                        vm.removeItem(id)

                    },
                    onCheckout = {
                        navController.navigate(NavRoutes.Checkout)
                    }
                )
            }
        }
    }
}

@Composable
private fun ErrorView(error: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Error: $error", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.error)
        Spacer(Modifier.height(12.dp))
        Button(onClick = onRetry) { Text("Retry") }
    }
}