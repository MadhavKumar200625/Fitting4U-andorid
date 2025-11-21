package com.fitting4u.fitting4u.presentation.Fabric.explore

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.fitting4u.fitting4u.Data.remote.dto.fabric.explore.Fabric
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


/* -------------------------------------------------------------------------- */
/*                              MAIN SCREEN                                   */
/* -------------------------------------------------------------------------- */

@Composable
fun FabricExploreScreen(
    navController: NavController,
    vm: FabricExploreViewModel = hiltViewModel(),

) {
    val state by vm.state.collectAsState()
    val gridState = rememberLazyGridState()
    val coroutine = rememberCoroutineScope()

    var localQuery by rememberSaveable { mutableStateOf("") }

    var sheetVisible by remember { mutableStateOf(false) }

    // this holds what user has applied (real applied filters)
    val appliedFilters = remember { mutableStateMapOf<String, String?>() }

    /* ---------------- Search Debounce ---------------- */
    LaunchedEffect(localQuery) {
        val text = localQuery
        delay(300)
        if (text == localQuery) {
            vm.setSearch(if (text.isBlank()) null else text)
        }
    }

    /* ---------------- Pagination ---------------- */
    LaunchedEffect(gridState, state.isLoading, state.page, state.totalPages) {
        snapshotFlow { gridState.layoutInfo }
            .map { it.visibleItemsInfo.lastOrNull()?.index ?: -1 }
            .distinctUntilChanged()
            .collectLatest { lastVisible ->
                val total = gridState.layoutInfo.totalItemsCount
                if (lastVisible >= total - 6 && !state.isLoading && state.page < state.totalPages) {
                    vm.loadNextPage()
                }
            }
    }

    /* ------------------- UI ---------------------- */

    Box(
        Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
    ) {
        Column(Modifier.fillMaxSize()) {

            Spacer(Modifier.height(18.dp))

            SearchBar(
                query = localQuery,
                onQueryChange = { localQuery = it },
                onFiltersClick = { sheetVisible = true },
                onClear = { localQuery = ""; vm.setSearch(null) }
            )

            if (appliedFilters.isNotEmpty()) {
                AppliedFiltersRow(
                    applied = appliedFilters,
                    onRemove = { key ->
                        appliedFilters.remove(key)
                        vm.updateFilter(key, null)
                    }
                )
            }

            if (state.isLoading && state.fabrics.isEmpty()) {
                Box(Modifier.fillMaxSize(), Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF003466))
                }
            } else {
                FabricList(
                    state = state,
                    gridState = gridState,
                    onItemClick = {navController.navigate("fabricDetail/${it.slug}")}
                )
            }
        }

        /* ------------------ FILTER SHEET ------------------ */
        FilterBottomSheet(
            visible = sheetVisible,
            filters = state.filters,
            appliedFilters = appliedFilters,
            onDismissRequest = { sheetVisible = false },
            onApply = { newMap ->
                coroutine.launch {
                    appliedFilters.clear()
                    appliedFilters.putAll(newMap)

                    // update VM filters
                    newMap.forEach { (key, value) ->
                        vm.updateFilter(key, value)
                    }

                    vm.setSearch(if (localQuery.isBlank()) null else localQuery)

                    sheetVisible = false
                }
            },
            onClearAll = {
                coroutine.launch {
                    appliedFilters.clear()
                    vm.refresh()
                    sheetVisible = false
                }
            }
        )
    }
}

/* -------------------------------------------------------------------------- */
/*                           SEARCH BAR                                       */
/* -------------------------------------------------------------------------- */

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onFiltersClick: () -> Unit,
    onClear: () -> Unit
) {
    val blue = Color(0xFF003466)
    var isFocused by remember { mutableStateOf(false) }

    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {

        Box(
            modifier = Modifier
                .weight(1f)
                .shadow(
                    elevation = 12.dp,
                    shape = RoundedCornerShape(26.dp),
                    ambientColor = Color.Black.copy(alpha = 0.10f),
                    spotColor = Color.Black.copy(alpha = 0.18f)
                )
                .clip(RoundedCornerShape(26.dp))
                .background(
                    Brush.linearGradient(
                        listOf(
                            Color.White,
                            Color(0xFFF9FBFF)
                        )
                    )
                )
                .border(
                    width = 1.dp,
                    color = if (isFocused) blue.copy(alpha = 0.45f) else Color(0xFFE4E9F1),
                    shape = RoundedCornerShape(26.dp)
                )
                .padding(horizontal = 18.dp, vertical = 10.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {

                Icon(
                    Icons.Default.Search,
                    contentDescription = null,
                    tint = blue.copy(alpha = 0.7f),
                    modifier = Modifier.size(22.dp)
                )

                Spacer(Modifier.width(12.dp))

                TextField(
                    value = query,
                    onValueChange = { onQueryChange(it) },
                    placeholder = {
                        Text(
                            "Search fabrics, prints, colors",
                            color = Color(0xFF6A7A90).copy(alpha = 0.55f)
                        )
                    },
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .onFocusChanged { isFocused = it.isFocused },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = blue,
                        unfocusedTextColor = blue,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        cursorColor = blue,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    )
                )

                AnimatedVisibility(query.isNotBlank()) {
                    Text(
                        "Clear",
                        modifier = Modifier
                            .clip(RoundedCornerShape(30.dp))
                            .clickable { onClear() }
                            .padding(horizontal = 6.dp, vertical = 4.dp),
                        color = blue,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(Modifier.width(10.dp))

                // PREMIUM FILTER ICON BUTTON
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(blue.copy(alpha = 0.08f))
                        .clickable { onFiltersClick() }
                        .padding(8.dp)
                ) {
                    Icon(
                        Icons.Default.FilterList,
                        contentDescription = "Filters",
                        tint = blue,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}