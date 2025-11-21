package com.fitting4u.fitting4u.presentation.Fabric.cart

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fitting4u.fitting4u.Data.remote.dto.fabric.cart.GetCartDto

@Composable
fun CartContent(
    data: GetCartDto,
    onIncrease: (id: String, delta: Double) -> Unit,
    onDecrease: (id: String, minus: Double) -> Unit,
    onRemove: (id: String) -> Unit,
    onCheckout: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()        // ⭐ top padding
            .navigationBarsPadding()    // ⭐ bottom padding
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(
                top = 12.dp,
                bottom = 160.dp  // ⭐ space for bottom nav + floating checkout bar
            )
        ) {

            items(data.items) { item ->
                CartItemCard(
                    id = item._id.toString(),
                    name = item.name,
                    image = item.image,
                    price = item.customerPrice,
                    material = item.material,
                    subtotal = item.subtotal,
                    qty = item.qty,
                    onIncrease = { onIncrease(item._id.toString(), 0.25) },
                    onDecrease = { onDecrease(item._id.toString(), 0.25) },
                    onRemove = { onRemove(item._id.toString()) }
                )
            }

            item {
                Spacer(Modifier.height(8.dp))
                OrderSummaryCard(
                    subtotal = data.bill.subtotal,
                    delivery = data.bill.delivery,
                    total = data.bill.total,
                    onCheckout = onCheckout,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Extra spacing at bottom for safety
            item { Spacer(Modifier.height(40.dp)) }
        }

        // ⭐ Floating bar (will appear above bottom nav safely)

    }
}