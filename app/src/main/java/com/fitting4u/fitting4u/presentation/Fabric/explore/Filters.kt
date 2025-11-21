package com.fitting4u.fitting4u.presentation.Fabric.explore

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun AppliedFiltersRow(
    applied: Map<String, String?>,
    onRemove: (String) -> Unit
) {
    val blue = Color(0xFF003466)

    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        applied.forEach { (key, value) ->
            if (!value.isNullOrBlank()) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    tonalElevation = 3.dp,
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Row(
                        Modifier
                            .background(Color.White)
                            .border(1.dp, Color(0xFFE6ECF3), RoundedCornerShape(16.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("${key}: $value", color = blue)

                        Spacer(Modifier.width(8.dp))

                        Text(
                            "×",
                            color = blue,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { onRemove(key) }
                        )
                    }
                }
            }
        }
    }
}



@Composable
fun FilterBottomSheet(
    visible: Boolean,
    filters: Map<String, List<String>>,
    appliedFilters: MutableMap<String, String?>,
    onDismissRequest: () -> Unit,
    onApply: (Map<String, String?>) -> Unit,
    onClearAll: () -> Unit
) {
    if (!visible) return

    val coroutine = rememberCoroutineScope()
    val density = LocalDensity.current

    // Local selected before Apply
    val localSelected = remember { mutableStateMapOf<String, String?>() }

    // sync localSelected when sheet opens
    LaunchedEffect(visible) {
        localSelected.clear()
        appliedFilters.forEach { (k, v) -> localSelected[k] = v }
    }

    val sheetHeight = 420.dp
    val sheetOffset = remember { Animatable(1f) }

    LaunchedEffect(visible) {
        sheetOffset.snapTo(1f)
        sheetOffset.animateTo(0f, tween(250))
    }

    Box(Modifier.fillMaxSize()) {

        // Dim background
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.45f))
                .clickable {
                    coroutine.launch {
                        sheetOffset.animateTo(1f, tween(200))
                        onDismissRequest()
                    }
                }
        )

        // Sheet
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset {
                    val px = with(density) { sheetHeight.toPx() }
                    IntOffset(0, (sheetOffset.value * px).toInt())
                }
                .fillMaxWidth()
                .height(sheetHeight)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
                .border(1.dp, Color(0xFFE8EDF3), RoundedCornerShape(20.dp))
        ) {

            Column(Modifier.fillMaxSize().padding(16.dp)) {

                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Filters", fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                    IconButton(onClick = {
                        coroutine.launch {
                            sheetOffset.animateTo(1f, tween(200))
                            onDismissRequest()
                        }
                    }) { Icon(Icons.Default.Close, null) }
                }

                Spacer(Modifier.height(6.dp))

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Text("Clear all", color = Color.Red, modifier = Modifier.clickable {
                        localSelected.clear()
                        onClearAll()
                    })
                }

                Spacer(Modifier.height(10.dp))

                Column(
                    Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    filters.forEach { (key, list) ->
                        if (list.isNotEmpty()) {
                            Text(key.uppercase(), fontWeight = FontWeight.Medium)
                            Spacer(Modifier.height(6.dp))

                            Row(
                                Modifier.horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                list.forEach { value ->

                                    val isSelected = localSelected[key] == value

                                    FilterChipItem(
                                        label = value,
                                        selected = isSelected,
                                        onXClick = {
                                            localSelected.remove(key)
                                            appliedFilters.remove(key)
                                        },
                                        onClick = {
                                            if (isSelected) {
                                                localSelected.remove(key)
                                            } else {
                                                localSelected[key] = value
                                            }
                                        }
                                    )
                                }
                            }
                            Spacer(Modifier.height(12.dp))
                        }
                    }
                }

                Button(
                    onClick = {
                        coroutine.launch {
                            sheetOffset.animateTo(1f, tween(200))
                            onApply(localSelected.toMap())
                        }
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Apply")
                }
            }
        }
    }
}

/* -------------------------------------------------------------------------- */
/*                           CHIP WITH X BUTTON                                */
/* -------------------------------------------------------------------------- */

@Composable
fun FilterChipItem(
    label: String,
    selected: Boolean,
    onXClick: () -> Unit,
    onClick: () -> Unit
) {
    val bg = if (selected) Color(0xFF003466) else Color.White
    val txt = if (selected) Color.White else Color(0xFF003466)

    Box(
        Modifier
            .clip(RoundedCornerShape(18.dp))
            .background(bg)
            .border(1.dp, Color(0xFFE6ECF3), RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(label, color = txt)
            if (selected) {
                Spacer(Modifier.width(6.dp))
                Text("×",
                    color = txt,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onXClick() }
                )
            }
        }
    }
}