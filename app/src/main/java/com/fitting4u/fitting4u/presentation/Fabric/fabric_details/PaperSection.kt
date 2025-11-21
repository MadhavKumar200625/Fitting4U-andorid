package com.fitting4u.fitting4u.presentation.Fabric.fabric_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fitting4u.fitting4u.ui.theme.PrimaryBlue

/* ===================== Paper Section Wrapper (title + content) ===================== */
@Composable
fun PaperSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    val primaryBlue = PrimaryBlue

    Card(
        modifier = Modifier.Companion
            .padding(horizontal = 20.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Companion.White),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.Companion
                .background(
                    Brush.Companion.verticalGradient(
                        listOf(
                            Color.Companion.White,
                            Color(0xFFF9FAFB),
                            Color(0xFFF4F7FA)
                        )
                    )
                )
                .padding(18.dp)
        ) {

            // Title row with accent bar
            Row(verticalAlignment = Alignment.Companion.CenterVertically) {
                Box(
                    modifier = Modifier.Companion
                        .width(4.dp)
                        .height(24.dp)
                        .background(
                            primaryBlue,
                            androidx.compose.foundation.shape.RoundedCornerShape(50)
                        )
                )
                Spacer(Modifier.Companion.width(10.dp))
                Text(
                    title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = primaryBlue,
                        fontWeight = FontWeight.Companion.SemiBold
                    )
                )
            }

            Spacer(Modifier.Companion.height(16.dp))

            content()
        }
    }
}