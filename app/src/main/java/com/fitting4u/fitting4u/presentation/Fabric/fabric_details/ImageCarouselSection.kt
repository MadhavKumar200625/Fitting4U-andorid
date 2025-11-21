package com.fitting4u.fitting4u.presentation.Fabric.fabric_details

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.fitting4u.fitting4u.ui.theme.PrimaryBlue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/* ===================== Image Carousel Section ===================== */
@Composable
fun ImageCarouselSection(images: List<String>?, name: String) {
    val list = images ?: listOf()
    if (list.isEmpty()) return

    val lazyState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var current by rememberSaveable { mutableStateOf(0) }

    // auto-scroll every 4s (nice effect)
    LaunchedEffect(list.size) {
        while (true) {
            delay(4000)
            val next = (current + 1) % list.size
            current = next
            coroutineScope.launch {
                // scroll by index
                lazyState.animateScrollToItem(next)
            }
        }
    }

    Box(
        modifier = Modifier.Companion
            .fillMaxWidth()
            .height(420.dp)
    ) {
        LazyRow(
            state = lazyState,
            modifier = Modifier.Companion.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            items(list.size) { idx ->
                val img = list[idx]
                Box(
                    modifier = Modifier.Companion
                        .fillParentMaxSize()
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(img)
                            .crossfade(true)
                            .build(),
                        contentDescription = "$name image $idx",
                        modifier = Modifier.Companion
                            .fillMaxSize()
                            .clip(RoundedCornerShape(18.dp)),
                        contentScale = ContentScale.Companion.Crop
                    )
                }
            }
        }

        // Dots indicator
        Row(
            modifier = Modifier.Companion
                .align(Alignment.Companion.BottomCenter)
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            list.forEachIndexed { i, _ ->
                val active = i == current
                val width by animateFloatAsState(
                    targetValue = if (active) 18f else 8f,
                    animationSpec = tween(250)
                )
                Box(
                    modifier = Modifier.Companion
                        .height(8.dp)
                        .width(width.dp)
                        .clip(androidx.compose.foundation.shape.RoundedCornerShape(10.dp))
                        .background(if (active) PrimaryBlue else Color(0xFFD1D5DB))
                )
            }
        }
    }
}