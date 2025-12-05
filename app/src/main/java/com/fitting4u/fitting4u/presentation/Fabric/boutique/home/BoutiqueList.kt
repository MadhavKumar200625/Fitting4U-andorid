package com.fitting4u.fitting4u.presentation.Fabric.boutique.home

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.fitting4u.fitting4u.Data.remote.dto.boutique.search.Boutique
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

@Composable
fun BoutiqueList(
    boutiques: List<Boutique>,
    page: Int,
    totalPages: Int,
    loading: Boolean,
    onLoadMore: (Int) -> Unit,
    onClick: (String) -> Unit
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    var lastRequestedPage by remember { mutableStateOf<Int?>(null) }
    LaunchedEffect(boutiques) {
        lastRequestedPage = null       // 👈 fix pagination freeze
    }
    /** AUTO PAGINATION */
    LaunchedEffect(listState, loading, page, totalPages) {
        snapshotFlow { listState.firstVisibleItemIndex to listState.layoutInfo.totalItemsCount }
            .map { (first, total) ->
                val visibleLast = first + listState.layoutInfo.visibleItemsInfo.size
                Triple(first, visibleLast, total)
            }
            .distinctUntilChanged()
            .filter { (_, visibleLast, total) ->
                visibleLast >= total - 2 && !loading && page < totalPages
            }
            .collect {
                val next = page + 1
                if (lastRequestedPage != next) {
                    lastRequestedPage = next
                    onLoadMore(next)
                }
            }
    }

    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = 8.dp,
            bottom = 120.dp // keeps bottom nav free
        ),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        itemsIndexed(
            items = boutiques,
            key = { _, item -> item._id }
        ) { index, item ->

            var scale by remember { mutableStateOf(0.9f) }

            LaunchedEffect(Unit) {
                scale = 1f
            }

            BoutiqueCard(
                b = item,
                onClick = onClick,
                modifier = Modifier.scale(
                    animateFloatAsState(
                        targetValue = scale,
                        animationSpec = tween(
                            durationMillis = 420,
                            easing = FastOutSlowInEasing,
                            delayMillis = (index % 8) * 40
                        )
                    ).value
                )
            )
        }

        if (loading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF003466))
                }
            }
        }
    }
}