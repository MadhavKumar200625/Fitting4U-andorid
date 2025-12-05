package com.fitting4u.fitting4u.presentation.Fabric.cart

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fitting4u.fitting4u.presentation.Fabric.Checkout.CheckoutUiState
import com.fitting4u.fitting4u.ui.theme.PrimaryBlue
@Composable
fun OrderSummaryCard(
    subtotal: Double,
    delivery: Double,
    total: Double,
    ui: CheckoutUiState,
    onProceedToShipping: () -> Unit,
    onPay: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.97f)
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                "Order Summary",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = PrimaryBlue
            )

            SummaryRow("Subtotal", "₹${"%.2f".format(subtotal)}")
            SummaryRow("Delivery", if (delivery <= 0.0) "Free" else "₹$delivery")

            Divider(
                color = Color(0xFFE1E5E9),
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total", fontWeight = FontWeight.Bold, color = PrimaryBlue)
                Text("₹${"%.2f".format(total)}", fontWeight = FontWeight.Bold, color = PrimaryBlue)
            }

            // ⭐ MAIN ACTION BUTTON
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),

                onClick = {
                    if (!(ui.shippingConfirmed)) {
                        onProceedToShipping()
                    } else {
                        onPay()
                    }
                }
            ) {
                Text(
                    text = if (!ui.shippingConfirmed)
                        "Proceed to Shipping"
                    else
                        "Pay ₹${"%.2f".format(total)}",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: String) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            label,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF6B7280)
        )
        Text(
            value,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            color = Color(0xFF111827)
        )
    }
}