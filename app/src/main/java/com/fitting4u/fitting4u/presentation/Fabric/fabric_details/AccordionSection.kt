package com.fitting4u.fitting4u.presentation.Fabric.fabric_details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/* ===================== Accordion (collapsible) ===================== */
@Composable
fun AccordionSection(
    title: String,
    contentComposable: @Composable () -> Unit
) {
    var open by remember { mutableStateOf(false) }

    val primaryBlue = Color(0xFF003466)

    Card(
        modifier = Modifier.Companion
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(18.dp),
                ambientColor = Color.Companion.Black.copy(alpha = 0.10f),
                spotColor = Color.Companion.Black.copy(alpha = 0.18f)
            ),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Companion.White
        )
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
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {

            // Header Row
            Row(
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .clickable { open = !open },
                verticalAlignment = Alignment.Companion.CenterVertically
            ) {

                // Accent Bar
                Box(
                    modifier = Modifier.Companion
                        .width(4.dp)
                        .height(26.dp)
                        .background(
                            primaryBlue,
                            androidx.compose.foundation.shape.RoundedCornerShape(50)
                        )
                )

                Spacer(Modifier.Companion.width(10.dp))

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = primaryBlue,
                        fontWeight = FontWeight.Companion.SemiBold
                    ),
                    modifier = Modifier.Companion.weight(1f)
                )

                Icon(
                    imageVector =
                        if (open) Icons.Default.KeyboardArrowUp
                        else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = primaryBlue,
                    modifier = Modifier.Companion.size(26.dp)
                )
            }

            AnimatedVisibility(open) {

                Column(
                    modifier = Modifier.Companion
                        .padding(top = 14.dp)
                        .fillMaxWidth()
                ) {

                    Divider(
                        color = Color(0xFFE5E7EB),
                        thickness = 1.dp
                    )

                    Spacer(Modifier.Companion.height(12.dp))

                    contentComposable()

                    Spacer(Modifier.Companion.height(6.dp))
                }
            }
        }
    }
}