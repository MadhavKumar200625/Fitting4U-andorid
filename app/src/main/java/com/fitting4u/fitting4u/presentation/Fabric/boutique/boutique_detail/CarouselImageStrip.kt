package com.fitting4u.fitting4u.presentation.Fabric.boutique.boutique_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CarouselImageStrip(
    images: List<String>,
    height: Dp = 280.dp,
    autoScrollInterval: Long = 3500L,
    onIndexChanged: (Int) -> Unit = {}
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    if (images.isEmpty()) return

    // current index tracked manually
    var currentIndex by remember { mutableStateOf(0) }

    // Auto-scroll effect
    LaunchedEffect(images) {
        if (images.size <= 1) return@LaunchedEffect
        while (true) {
            delay(autoScrollInterval)
            currentIndex = (currentIndex + 1) % images.size
            onIndexChanged(currentIndex)
            scope.launch {
                try {
                    listState.animateScrollToItem(currentIndex)
                } catch (_: Exception) { }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        // IMAGE STRIP
        LazyRow(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
        ) {
            itemsIndexed(images) { index, url ->
                AsyncImage(
                    model = url,
                    contentDescription = "Image $index",
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .height(height),
                    contentScale = ContentScale.Crop
                )
            }
        }

        // INDICATORS
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            images.forEachIndexed { i, _ ->
                val selected = i == currentIndex
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(if (selected) 10.dp else 8.dp)
                        .clip(RoundedCornerShape(50))
                        .background(
                            if (selected) Primary
                            else Primary.copy(alpha = 0.25f)
                        )
                )
            }
        }
    }
}