package com.fitting4u.fitting4u.presentation.Fabric

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fitting4u.fitting4u.ui.theme.PrimaryBlue

@Composable
fun FloatingCartSummaryBar(
    totalQty: Double,
    onClickGoToCart: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 110.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Button(
            onClick = onClickGoToCart,
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .height(54.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(PrimaryBlue)
        ) {
            Text("${"%.2f".format(totalQty)} m in cart • Go to Cart")
        }
    }
}